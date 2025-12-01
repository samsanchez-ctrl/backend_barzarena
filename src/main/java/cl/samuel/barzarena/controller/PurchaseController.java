package cl.samuel.barzarena.controller;

import cl.samuel.barzarena.dto.PurchaseResponse;
import cl.samuel.barzarena.model.User;
import cl.samuel.barzarena.repository.UserRepository;
import cl.samuel.barzarena.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final UserRepository userRepository;

    private User getUser(UserDetails details) {
        return userRepository.findByUsername(details.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // Checkout: usa el saldo del usuario y vacía carrito
    @PostMapping("/checkout")
    public PurchaseResponse checkout(@AuthenticationPrincipal UserDetails details) {
        User u = getUser(details);
        return purchaseService.checkout(u);
    }

    // Mis compras
    @GetMapping("/me")
    public List<PurchaseResponse> myPurchases(@AuthenticationPrincipal UserDetails details) {
        User u = getUser(details);
        return purchaseService.getMyPurchases(u);
    }

    // Obtener compra por id (propietario o admin)
    @GetMapping("/{id}")
    public PurchaseResponse getById(@AuthenticationPrincipal UserDetails details,
                                    @PathVariable Long id) {
        User u = getUser(details);
        return purchaseService.getById(id, u);
    }

    // Admin: ver todas las compras
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<PurchaseResponse> allPurchases() {
        return purchaseService.getAllPurchases();
    }
}

