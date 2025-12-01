package cl.samuel.barzarena.controller;


import cl.samuel.barzarena.dto.AddBalanceRequest;
import cl.samuel.barzarena.dto.SpendBalanceRequest;
import cl.samuel.barzarena.model.User;
import cl.samuel.barzarena.repository.UserRepository;
import cl.samuel.barzarena.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/me")
    public User getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {

        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
    @PostMapping("/add-balance")
    public User addBalance(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody AddBalanceRequest request
    ) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return userService.addBalance(user, request.getAmount());
    }

    @PostMapping("/spend")
    public User spendBalance(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody SpendBalanceRequest request
    ) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return userService.spendBalance(user, request.getAmount());
    }
}



