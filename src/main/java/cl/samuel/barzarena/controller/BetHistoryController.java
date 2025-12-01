package cl.samuel.barzarena.controller;

import cl.samuel.barzarena.model.Bet;
import cl.samuel.barzarena.model.User;
import cl.samuel.barzarena.repository.BetRepository;
import cl.samuel.barzarena.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bets/history")
@RequiredArgsConstructor
public class BetHistoryController {

    private final BetRepository betRepository;
    private final UserRepository userRepository;

    @GetMapping
    public List<Bet> getMyBets(@AuthenticationPrincipal UserDetails details) {
        User u = userRepository.findByUsername(details.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return betRepository.findAll()
                .stream()
                .filter(b -> b.getUser().getId().equals(u.getId()))
                .toList();
    }
}
