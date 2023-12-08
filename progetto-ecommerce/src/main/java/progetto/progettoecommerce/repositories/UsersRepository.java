package progetto.progettoecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import progetto.progettoecommerce.entities.User;

@Repository
public interface UsersRepository extends JpaRepository <User, Integer> {
    User findByEmail(String email);
}
