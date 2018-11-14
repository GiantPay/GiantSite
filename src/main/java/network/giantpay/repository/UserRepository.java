package network.giantpay.repository;

import network.giantpay.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username,String email);
}
