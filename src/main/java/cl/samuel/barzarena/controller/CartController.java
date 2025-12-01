package cl.samuel.barzarena.controller;

import cl.samuel.barzarena.dto.CartResponse;
import cl.samuel.barzarena.model.User;
import cl.samuel.barzarena.repository.UserRepository;
import cl.samuel.barzarena.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;

    private User getUser(UserDetails details) {
        return userRepository.findByUsername(details.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @GetMapping
    public CartResponse getCart(@AuthenticationPrincipal UserDetails details) {
        return cartService.getCart(getUser(details));
    }

    @PostMapping("/add")
    public CartResponse addItem(
            @AuthenticationPrincipal UserDetails details,
            @RequestParam Long productId,
            @RequestParam Integer qty
    ) {
        return cartService.addItem(getUser(details), productId, qty);
    }

    @PutMapping("/item/{itemId}")
    public CartResponse updateItem(
            @AuthenticationPrincipal UserDetails details,
            @PathVariable Long itemId,
            @RequestParam Integer qty
    ) {
        return cartService.updateItem(getUser(details), itemId, qty);
    }

    @DeleteMapping("/item/{itemId}")
    public CartResponse deleteItem(
            @AuthenticationPrincipal UserDetails details,
            @PathVariable Long itemId
    ) {
        return cartService.removeItem(getUser(details), itemId);
    }

    @DeleteMapping("/clear")
    public CartResponse clear(@AuthenticationPrincipal UserDetails details) {
        return cartService.clearCart(getUser(details));
    }
}

