package consegna.progettoecommerce.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import consegna.progettoecommerce.entities.Product;;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
    
    Product findByCode(String code);
    Product findByModel(String model);
    Page<Product> findByName(String name, PageRequest pageRequest);
    Page<Product> findByType(String type, PageRequest pageRequest);
    Page<Product> findAll(Pageable pageable);
}
