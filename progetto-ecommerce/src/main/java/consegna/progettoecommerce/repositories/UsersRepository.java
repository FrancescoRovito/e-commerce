package consegna.progettoecommerce.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import consegna.progettoecommerce.entities.User;

@Repository
public interface UsersRepository extends JpaRepository <User, Integer> {
    User findByEmail(String email);
    Page<User> findAll(Pageable pageable);
    //List<User> findAll();
}
