package cl.samuel.barzarena.service;

import cl.samuel.barzarena.dto.BetRequest;
import cl.samuel.barzarena.dto.BetResponse;
import cl.samuel.barzarena.model.*;
import cl.samuel.barzarena.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BetService {

    private final BetRepository betRepository;
    private final BattleRepository battleRepository;
    private final UserRepository userRepository;

    private BetResponse map(Bet b) {
        return new BetResponse(
                b.getId(),
                b.getUser().getUsername(),
                b.getBattle().getId(),
                b.getBattle().getDate().toString(),
                b.getPrediction(),
                b.getAmount(),
                b.isPaid()
        );
    }

    public List<BetResponse> getMyBets(User user) {
        return betRepository.findByUser(user).stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public List<BetResponse> getAllBetsAdmin() {
        return betRepository.findAll().stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public BetResponse getById(Long id, User requester) {
        Bet b = betRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Apuesta no encontrada"));
        boolean isOwner = b.getUser().getId().equals(requester.getId());
        boolean isAdmin = requester.getRoles().stream()
                .anyMatch(r -> r.getName().name().equals("ROLE_ADMIN"));

        if (!isOwner && !isAdmin) throw new RuntimeException("No autorizado para ver esta apuesta");

        return map(b);
    }

    @Transactional
    public BetResponse placeBet(User user, BetRequest req) {

        Battle battle = battleRepository.findById(req.getBattleId())
                .orElseThrow(() -> new RuntimeException("Batalla no encontrada"));

        if (battle.getWinner() != Battle.BattleWinner.NONE) {
            throw new RuntimeException("No se puede apostar en una batalla que ya tiene ganador");
        }

        if (!battle.getActive()) {
            throw new RuntimeException("La batalla no está activa para apuestas");
        }

        if (req.getAmount() <= 0) {
            throw new RuntimeException("El monto debe ser mayor que 0");
        }

        if (user.getSaldo() < req.getAmount()) {
            throw new RuntimeException("Saldo insuficiente para realizar la apuesta");
        }

        // Descontar saldo inmediatamente
        user.setSaldo(user.getSaldo() - req.getAmount());
        userRepository.save(user);

        Bet bet = new Bet();
        bet.setUser(user);
        bet.setBattle(battle);
        bet.setPrediction(req.getPrediction());
        bet.setAmount(req.getAmount());
        bet.setPaid(false);

        Bet saved = betRepository.save(bet);

        return map(saved);
    }

    @Transactional
    public void cancelBet(User user, Long betId) {
        Bet b = betRepository.findById(betId)
                .orElseThrow(() -> new RuntimeException("Apuesta no encontrada"));

        if (!b.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("No autorizado para cancelar esta apuesta");
        }

        if (b.getBattle().getWinner() != Battle.BattleWinner.NONE) {
            throw new RuntimeException("No se puede cancelar una apuesta sobre una batalla ya decidida");
        }

        // Reembolsar saldo al usuario
        User u = b.getUser();
        u.setSaldo(u.getSaldo() + b.getAmount());
        userRepository.save(u);

        betRepository.delete(b);
    }

    // 💥 NUEVO — Paga apuestas al asignar ganador
    @Transactional
    public void processBetsForBattle(Battle battle) {

        List<Bet> bets = betRepository.findByBattle(battle);

        for (Bet bet : bets) {

            // Si ya está pagada, no hacer nada
            if (bet.isPaid()) continue;

            // Apuesta ganada
            if (bet.getPrediction() == battle.getWinner()) {

                User user = bet.getUser();
                double reward = bet.getAmount() * 2; // pago simple: x2

                user.setSaldo(user.getSaldo() + reward);
                userRepository.save(user);

                bet.setPaid(true);
                betRepository.save(bet);
            }
        }
    }
}


