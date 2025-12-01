package cl.samuel.barzarena.repository;

import cl.samuel.barzarena.model.Bet;
import cl.samuel.barzarena.model.Battle;
import cl.samuel.barzarena.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BetRepository extends JpaRepository<Bet, Long> {

    List<Bet> findByUser(User user);

    List<Bet> findByBattle(Battle battle);

}

