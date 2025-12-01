package cl.samuel.barzarena.repository;

import cl.samuel.barzarena.model.Rapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface RapperRepository extends JpaRepository<Rapper, Long> {
    Optional<Rapper> findByName(String name);
    boolean existsByName(String name);
    List<Rapper> findByActiveTrue();
}

