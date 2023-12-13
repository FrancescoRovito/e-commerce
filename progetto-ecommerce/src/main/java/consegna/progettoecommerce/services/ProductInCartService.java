package consegna.progettoecommerce.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import consegna.progettoecommerce.entities.Product;
import consegna.progettoecommerce.entities.ProductInCart;
import consegna.progettoecommerce.entities.Purchase;
import consegna.progettoecommerce.entities.User;
import consegna.progettoecommerce.models.requests.PageRequestAttributes;
import consegna.progettoecommerce.models.requests.ProductInCartRequest;
import consegna.progettoecommerce.repositories.ProductInCartRepository;
import consegna.progettoecommerce.repositories.ProductRepository;
import consegna.progettoecommerce.repositories.PurchaseRepository;
import consegna.progettoecommerce.repositories.UsersRepository;
import consegna.progettoecommerce.utility.exceptions.BudgetNotAvailableException;
import consegna.progettoecommerce.utility.exceptions.DataNotCorrectException;
import consegna.progettoecommerce.utility.exceptions.ProductInCartNotExistsExceptions;
import consegna.progettoecommerce.utility.exceptions.ProductNotAvailableException;
import consegna.progettoecommerce.utility.exceptions.ProductNotExistsException;
import consegna.progettoecommerce.utility.exceptions.UserNotExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductInCartService {

    private final UsersRepository usersRepository;
    private final ProductRepository productRepository;
    private final ProductInCartRepository productInCartRepository;
    private final PurchaseRepository purchaseRepository;
    
    @Transactional
    public List<ProductInCart> addProductInCart(String email, ProductInCartRequest productInCartRequest) throws RuntimeException{
        if(productInCartRequest.getQuantityToPurchase()<=0)
            throw new DataNotCorrectException();
        User user=usersRepository.findByEmail(email);
        if(user == null)
            throw new UserNotExistsException();
        Product prod=productRepository.findByCode(productInCartRequest.getProductCode());
        if(prod==null)
            throw new ProductNotExistsException();
        ProductInCart prodCart=productInCartRepository.findByUserAndProduct(user, prod);
        if(prodCart==null){ //se ancora non ho aggiunto il prod in carrello
            //se la qta da acquistare è disponibile
            if(prod.getQuantity()>=productInCartRequest.getQuantityToPurchase()){
                prodCart=ProductInCart.builder().product(prod)
                .user(user).quantityToPurchase(productInCartRequest.getQuantityToPurchase())
                .build();
                productInCartRepository.save(prodCart);
                user.getProductsInCart().add(prodCart); //aggiungo nel carrello dell'utente
                user=usersRepository.save(user);
            }
            else{   //qta non disponibile
                throw new ProductNotAvailableException();
            }  
        }
        //se il prod è già nel carrello controlla la qta
        else if(prod.getQuantity()>=prodCart.getQuantityToPurchase()+productInCartRequest.getQuantityToPurchase()){
                prodCart.setQuantityToPurchase(prodCart.getQuantityToPurchase()+productInCartRequest.getQuantityToPurchase());
        }
        else{   //se il prod è nel carrello ma la nuova qta da acquistare non è disponibile
            throw new ProductNotAvailableException();
        }
        productInCartRepository.save(prodCart);
        return user.getProductsInCart();
    }

    public void deleteProductInCart(String email, String productCode) throws RuntimeException{
        User user=usersRepository.findByEmail(email);
        if(user==null)
            throw new UserNotExistsException();
        Product prod=productRepository.findByCode(productCode);
        if(prod==null)
            throw new ProductNotExistsException();
        ProductInCart prodCart=productInCartRepository.findByUserAndProduct(user, prod);
        if(prodCart==null)
            throw new ProductInCartNotExistsExceptions();
        productInCartRepository.delete(prodCart);
        user.getProductsInCart().remove(prodCart);
        usersRepository.save(user);
    }

    
    public Page<ProductInCart> getProductsInCart (String email, PageRequestAttributes pageRequestAttributes) throws RuntimeException{
        User user=usersRepository.findByEmail(email);
        if(user==null)
            throw new UserNotExistsException();
        PageRequest pageRequest=PageRequest.of(pageRequestAttributes.getPage(), pageRequestAttributes.getDimPage());
        return productInCartRepository.findByUser(user, pageRequest);
    } 

    //aggiusto l'if, aggiusto lo user dto
    //provo con due utenti diversi se cambio la qta e non è disponibile cosa succede
    //provo se non ho soldi
    @Transactional
    public User buyAllProductsInCart(String email) throws RuntimeException{
        User user=usersRepository.findByEmail(email);
        if(user==null)
            throw new UserNotExistsException();
        //se ho disponibilità economica
        if(user.getBudget()>=getTotalCost(email)){
            //salvo gli acquisti in purchase prendendoli dal carrello
            List<Product> productsToPurchase=new ArrayList<>();
            Product product=null;
            for(ProductInCart prodCart:user.getProductsInCart()){
                // se qta non è disponibile in magazzino
                //Product productInStore=productRepository.findByCode(prodCart.getProduct().getCode());
                product=prodCart.getProduct();
                 if(prodCart.getQuantityToPurchase()>product.getQuantity())
                    throw new ProductNotAvailableException();
                productsToPurchase.add(product);
                
                //aggiorno il magazzino dopo l'acquisto per ogni productInCart
                product.setQuantity(product.getQuantity()-prodCart.getQuantityToPurchase());
                productRepository.save(product);
            }
            Purchase purchase=Purchase.builder()
            .products(productsToPurchase).user(user).totalCost(getTotalCost(email)).build();
                
            purchaseRepository.save(purchase);
            user.getPurchase().add(purchase); //lo aggiungo all'utente
             //svuoto il carrello nel db
            productInCartRepository.deleteAllByUser(user);
            //aggiorno il budget
            user.setBudget(user.getBudget()-getTotalCost(email));
            user.setHasBuyed(true);
            //svuoto il carrello in ram
            user.getProductsInCart().clear();
        }
        else{
            throw new BudgetNotAvailableException();
        }
        return usersRepository.save(user);
    }

    public double getTotalCost(String email) throws RuntimeException{
        User user=usersRepository.findByEmail(email);
        if(user==null)
            throw new UserNotExistsException();
        double total=0;
        for(ProductInCart prodCart: user.getProductsInCart())
            total+=prodCart.getProduct().getPrice()*prodCart.getQuantityToPurchase();
        return total;
    }
}
