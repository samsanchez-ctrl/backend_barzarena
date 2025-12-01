package cl.samuel.barzarena.controller;

import cl.samuel.barzarena.dto.BetRequest;
import cl.samuel.barzarena.dto.BetResponse;
import cl.samuel.barzarena.model.User;
import cl.samuel.barzarena.repository.UserRepository;
import cl.samuel.barzarena.service.BetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bets")
@RequiredArgsConstructor
public class BetController {

    private final BetService betService;
    private final UserRepository userRepository;

    private User getUser(UserDetails details) {
        return userRepository.findByUsername(details.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // Colocar apuesta (usuario autenticado)
    @PostMapping("/place")
    public BetResponse placeBet(@AuthenticationPrincipal UserDetails details,
                                @Valid @RequestBody BetRequest req) {
        User u = getUser(details);
        return betService.placeBet(u, req);
    }

    // Listar mis apuestas
    @GetMapping("/me")
    public List<BetResponse> myBets(@AuthenticationPrincipal UserDetails details) {
        User u = getUser(details);
        return betService.getMyBets(u);
    }

    // Ver apuesta (dueño o admin)
    @GetMapping("/{id}")
    public BetResponse getOne(@AuthenticationPrincipal UserDetails details,
                              @PathVariable Long id) {
        User u = getUser(details);
        return betService.getById(id, u);
    }

    // Cancelar apuesta (solo dueño, solo si batalla no tiene ganador)
    @DeleteMapping("/{id}")
    public void cancel(@AuthenticationPrincipal UserDetails details,
                       @PathVariable Long id) {
        User u = getUser(details);
        betService.cancelBet(u, id);
    }

    // Admin: ver todas las apuestas
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<BetResponse> allBets() {
        return betService.getAllBetsAdmin();
    }
}

