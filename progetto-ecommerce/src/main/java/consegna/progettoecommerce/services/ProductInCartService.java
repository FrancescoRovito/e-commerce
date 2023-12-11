package consegna.progettoecommerce.services;

import java.util.List;

import org.springframework.stereotype.Service;

import consegna.progettoecommerce.entities.Product;
import consegna.progettoecommerce.entities.ProductInCart;
import consegna.progettoecommerce.entities.User;
import consegna.progettoecommerce.models.requests.ProductInCartRequest;
import consegna.progettoecommerce.repositories.ProductInCartRepository;
import consegna.progettoecommerce.repositories.ProductRepository;
import consegna.progettoecommerce.repositories.UsersRepository;
import consegna.progettoecommerce.utility.exceptions.DataNotCorrectException;
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


}
