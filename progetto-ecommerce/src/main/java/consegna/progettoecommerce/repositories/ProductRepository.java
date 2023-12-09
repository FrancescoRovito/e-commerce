package consegna.progettoecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import consegna.progettoecommerce.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
    
}
