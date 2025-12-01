package cl.samuel.barzarena.repository;

import cl.samuel.barzarena.model.Cart;
import cl.samuel.barzarena.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}

