package consegna.progettoecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import consegna.progettoecommerce.entities.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer>{
}
