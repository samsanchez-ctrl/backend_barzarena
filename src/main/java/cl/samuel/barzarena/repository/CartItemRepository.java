package cl.samuel.barzarena.repository;

import cl.samuel.barzarena.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}

