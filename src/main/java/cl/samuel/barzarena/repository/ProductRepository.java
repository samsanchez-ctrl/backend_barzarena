package cl.samuel.barzarena.repository;

import cl.samuel.barzarena.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

