package cl.samuel.barzarena.repository;

import cl.samuel.barzarena.model.Purchase;
import cl.samuel.barzarena.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByUser(User user);
}


