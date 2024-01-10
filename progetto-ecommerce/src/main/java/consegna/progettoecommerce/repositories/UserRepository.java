package consegna.progettoecommerce.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import consegna.progettoecommerce.entities.User;

@Repository
public interface UserRepository extends JpaRepository <User, Integer> {
    User findByEmail(String email);
    User findById(int id);
    Page<User> findAll(Pageable pageable);
    //List<User> findAll();
}
