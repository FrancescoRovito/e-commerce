package consegna.progettoecommerce.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import consegna.progettoecommerce.entities.Purchase;
import consegna.progettoecommerce.models.dots.PurchaseQueryDTO;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Integer>{
    @Query(value="SELECT purchase_date, total_cost, consumer "+ 
            "FROM PURCHASE P, USERS U "+ 
            "WHERE P.consumer=U.id And U.id=userId AND P.purchase_date=date", nativeQuery=true)
    List<Purchase> getByYear(@Param("userId") Integer userId, @Param("date") Date date);

    @Query(value="SELECT NEW PurchaseQueryDTO (p.total_cost) FROM purchase p", nativeQuery=true)
    List<PurchaseQueryDTO> getByTest();
    
    @Query(value="SELECT p.id FROM Purchase p WHERE p.consumer = 1", nativeQuery=true)
    List<Integer> findConsumer();
}
