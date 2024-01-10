package consegna.progettoecommerce.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import consegna.progettoecommerce.entities.Purchase;
import consegna.progettoecommerce.models.dots.PurchaseQueryDTO;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Integer>{
    /* OLD QUERY ERRATA
    @Query(value="SELECT purchase_date, total_cost, consumer "+ 
                 "FROM PURCHASE P, USERS U "+ 
                 "WHERE P.consumer=U.id And U.id=userId AND P.purchase_date=date", nativeQuery=true) 
    List<Purchase> getByYear(@Param("userId") Integer userId, @Param("date") Date date); */

    @Query(value="SELECT P.consumer AS Consumer, P.purchase_date AS PurchaseDate, P.total_cost AS TotalCost "+ 
                   "FROM PURCHASE P JOIN USERS U ON (P.consumer=U.id) "+ 
                   "WHERE U.id= :userId AND EXTRACT(YEAR FROM P.purchase_date)= :year", nativeQuery=true) 
    Page<PurchaseQueryDTO> getByYear(@Param("userId") Integer userId, @Param("year") int year, PageRequest pageRequest); 
    

    /*JPQL p.user.id fa riferimento all'oggetto che si trova in purchase e non al nome della colonna
    @Query("SELECT p.id FROM Purchase p WHERE p.user.id = :userId") */

    //@Query(value="SELECT p.id FROM Purchase p WHERE p.consumer = :userId", nativeQuery=true)
    @Query(value="SELECT p.id FROM Purchase p WHERE p.consumer = ?1", nativeQuery=true)
    List<Integer> findConsumer(@Param("userId")Integer userId);
}
