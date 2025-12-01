package cl.samuel.barzarena.repository;

import cl.samuel.barzarena.model.Battle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BattleRepository extends JpaRepository<Battle, Long> {
    List<Battle> findByActiveTrue();
}

