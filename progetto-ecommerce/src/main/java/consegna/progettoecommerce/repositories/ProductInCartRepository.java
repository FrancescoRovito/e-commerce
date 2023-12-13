package consegna.progettoecommerce.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import consegna.progettoecommerce.entities.Product;
import consegna.progettoecommerce.entities.ProductInCart;
import consegna.progettoecommerce.entities.User;

@Repository
public interface ProductInCartRepository extends JpaRepository<ProductInCart, Integer> {
    ProductInCart findByUserAndProduct(User user, Product product);
    void deleteAllByUser(User user);
    Page<ProductInCart> findByUser(User user, PageRequest pageRequest);
}
