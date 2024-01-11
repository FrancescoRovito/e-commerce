package consegna.progettoecommerce.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import consegna.progettoecommerce.entities.Product;
import consegna.progettoecommerce.entities.ProductInCart;
import consegna.progettoecommerce.entities.Purchase;
import consegna.progettoecommerce.entities.User;
import consegna.progettoecommerce.models.DtosMapper;
import consegna.progettoecommerce.models.dots.UserDTO;
import consegna.progettoecommerce.models.requests.PageRequestAttributes;
import consegna.progettoecommerce.models.requests.ProductInCartRequest;
import consegna.progettoecommerce.repositories.ProductInCartRepository;
import consegna.progettoecommerce.repositories.ProductRepository;
import consegna.progettoecommerce.repositories.PurchaseRepository;
import consegna.progettoecommerce.repositories.UserRepository;
import consegna.progettoecommerce.utility.exceptions.BudgetNotAvailableException;
import consegna.progettoecommerce.utility.exceptions.DataNotCorrectException;
import consegna.progettoecommerce.utility.exceptions.ProductInCartNotExistsException;
import consegna.progettoecommerce.utility.exceptions.ProductNotAvailableException;
import consegna.progettoecommerce.utility.exceptions.ProductNotExistsException;
import consegna.progettoecommerce.utility.exceptions.UserNotExistsException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductInCartService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductInCartRepository productInCartRepository;
    private final PurchaseRepository purchaseRepository;
    
    @Transactional
    public List<ProductInCart> addProductInCart(String email, ProductInCartRequest productInCartRequest) throws RuntimeException{
        if(productInCartRequest.getQuantityToPurchase()<=0)
            throw new DataNotCorrectException();
        User user=userRepository.findByEmail(email);
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
                user=userRepository.save(user);
            }
            else{   //qta non disponibile
                throw new ProductNotAvailableException();
            }  
        }
        //se il prod è già nel carrello controllo la qta
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
        User user=userRepository.findByEmail(email);
        if(user==null)
            throw new UserNotExistsException();
        Product prod=productRepository.findByCode(productCode);
        if(prod==null)
            throw new ProductNotExistsException();
        ProductInCart prodCart=productInCartRepository.findByUserAndProduct(user, prod);
        if(prodCart==null)
            throw new ProductInCartNotExistsException();
        productInCartRepository.delete(prodCart);
        user.getProductsInCart().remove(prodCart);
        userRepository.save(user);
    }

    public Page<ProductInCart> getProductsInCart (String email, PageRequestAttributes pageRequestAttributes) throws RuntimeException{
        User user=userRepository.findByEmail(email);
        if(user==null)
            throw new UserNotExistsException();
        PageRequest pageRequest=PageRequest.of(pageRequestAttributes.getPage(), pageRequestAttributes.getDimPage());
        return productInCartRepository.findByUser(user, pageRequest);
    } 

    @Transactional
    public UserDTO buyAllProductsInCart(String email) throws RuntimeException{
        User user=userRepository.findByEmail(email);
        if(user==null)
            throw new UserNotExistsException();

        if(user.getBudget()<getTotalCost(email))
              throw new BudgetNotAvailableException();
        
        //salvo gli acquisti in purchase prendendoli dal carrello
        List<Product> productsToPurchase=new ArrayList<>();
        Product product=null;
        for(ProductInCart prodCart:user.getProductsInCart()){
            // se qta non è disponibile in magazzino
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
        //svuoto il carrello in ram
        user.getProductsInCart().clear();
        
        userRepository.save(user);
        UserDTO userDTO=DtosMapper.INSTANCE.userToUserDTO(user);
        return userDTO;
    }

    @Transactional
    public UserDTO buySingleProductInCart(String email, String productCode) throws RuntimeException{
        User user=userRepository.findByEmail(email);
        if(user==null)
            throw new UserNotExistsException();
        Product product=productRepository.findByCode(productCode);
        if(product==null)
            throw new ProductNotExistsException();
        ProductInCart prodCart=productInCartRepository.findByUserAndProduct(user, product);
        if(prodCart==null)
            throw new ProductInCartNotExistsException();
        if(prodCart.getQuantityToPurchase()>product.getQuantity())
            throw new ProductNotAvailableException();
        //verifico se ho disponibilita economica
        double cost=prodCart.getProduct().getPrice()*prodCart.getQuantityToPurchase();
        if(user.getBudget()<cost)
            throw new BudgetNotAvailableException();
        
        //modifico la qta in magazzino
        product.setQuantity(product.getQuantity()-prodCart.getQuantityToPurchase());
        productRepository.save(product);

        Purchase purchase=Purchase.builder()
        .products(new ArrayList<>(Arrays.asList(product))).user(user).totalCost(cost).build();
        purchaseRepository.save(purchase);
        user.getPurchase().add(purchase); // lo aggiungo all'utente

        //modifico il budget
        user.setBudget(user.getBudget()-cost);

        //elimino il product dal carrello
        user.getProductsInCart().remove(prodCart);
        userRepository.save(user);
        productInCartRepository.delete(prodCart);

        UserDTO userDTO=DtosMapper.INSTANCE.userToUserDTO(user);
        return userDTO;
    }

    public double getTotalCost(String email) throws RuntimeException{
        User user=userRepository.findByEmail(email);
        if(user==null)
            throw new UserNotExistsException();
        double total=0;
        for(ProductInCart prodCart: user.getProductsInCart())
            total+=prodCart.getProduct().getPrice()*prodCart.getQuantityToPurchase();
        return total;
    }

    @Transactional
    public void modifyQuantityProductInCart(String email, ProductInCartRequest productInCartRequest) throws RuntimeException{
        if(productInCartRequest.getQuantityToPurchase()<0)
            throw new DataNotCorrectException();
        User user=userRepository.findByEmail(email);
        if(user==null)
            throw new UserNotExistsException();
        Product prod=productRepository.findByCode(productInCartRequest.getProductCode());
        if(prod==null)
            throw new ProductNotExistsException();
        ProductInCart prodCart=productInCartRepository.findByUserAndProduct(user, prod);
        if(prodCart==null)
            throw new ProductInCartNotExistsException();
        if(prod.getQuantity()<productInCartRequest.getQuantityToPurchase())
            throw new ProductNotAvailableException();
        //se=0 di fatto elimino il prodotto dal carrello
        if(productInCartRequest.getQuantityToPurchase()==0){
            productInCartRepository.delete(prodCart);
            user.getProductsInCart().remove(prodCart);
        }
        if(prod.getQuantity()>=productInCartRequest.getQuantityToPurchase()){
            prodCart.setQuantityToPurchase(productInCartRequest.getQuantityToPurchase());
            productInCartRepository.save(prodCart);
        }
        userRepository.save(user);
    }
}
