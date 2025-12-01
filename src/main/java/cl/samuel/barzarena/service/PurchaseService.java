package cl.samuel.barzarena.service;

import cl.samuel.barzarena.dto.PurchaseItemResponse;
import cl.samuel.barzarena.dto.PurchaseResponse;
import cl.samuel.barzarena.model.Cart;
import cl.samuel.barzarena.model.CartItem;
import cl.samuel.barzarena.model.Product;
import cl.samuel.barzarena.model.Purchase;
import cl.samuel.barzarena.model.PurchaseItem;
import cl.samuel.barzarena.model.User;
import cl.samuel.barzarena.repository.CartItemRepository;
import cl.samuel.barzarena.repository.CartRepository;
import cl.samuel.barzarena.repository.ProductRepository;
import cl.samuel.barzarena.repository.PurchaseItemRepository;
import cl.samuel.barzarena.repository.PurchaseRepository;
import cl.samuel.barzarena.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PurchaseRepository purchaseRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final CartItemRepository cartItemRepository;

    // map -> PurchaseResponse (método público para que otros servicios lo reutilicen)
    public PurchaseResponse mapPurchase(Purchase p) {
        List<PurchaseItemResponse> items = p.getItems().stream()
                .map(pi -> new PurchaseItemResponse(
                        pi.getProduct().getId(),
                        pi.getProduct().getName(),
                        pi.getQuantity(),
                        pi.getPriceAtPurchase(),
                        pi.getPriceAtPurchase() * pi.getQuantity()
                ))
                .collect(Collectors.toList());

        return new PurchaseResponse(
                p.getId(),
                p.getTotal(),
                p.getDate(),
                items
        );
    }

    @Transactional
    public PurchaseResponse checkout(User user) {

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Carrito vacío o no existe"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        double total = cart.getItems().stream()
                .mapToDouble(ci -> ci.getProduct().getPrice() * ci.getQuantity())
                .sum();

        if (user.getSaldo() < total) {
            throw new RuntimeException("Saldo insuficiente");
        }

        // Validar stock antes de proceder
        for (CartItem ci : cart.getItems()) {
            Product p = productRepository.findById(ci.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + ci.getProduct().getId()));

            if (p.getStock() < ci.getQuantity()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + p.getName());
            }
        }

        // Descontar saldo
        user.setSaldo(user.getSaldo() - total);
        userRepository.save(user);

        // Crear Purchase
        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setTotal(total);
        purchase.setDate(LocalDateTime.now());
        purchase = purchaseRepository.save(purchase);

        // Crear PurchaseItems y descontar stock
        for (CartItem ci : cart.getItems()) {

            Product p = productRepository.findById(ci.getProduct().getId()).get();

            // descontar stock
            p.setStock(p.getStock() - ci.getQuantity());
            productRepository.save(p);

            PurchaseItem pi = new PurchaseItem();
            pi.setPurchase(purchase);
            pi.setProduct(p);
            pi.setQuantity(ci.getQuantity());
            pi.setPriceAtPurchase(p.getPrice());
            purchaseItemRepository.save(pi);

            purchase.getItems().add(pi);
        }

        purchaseRepository.save(purchase);

        // limpiar carrito (borra items)
        cart.getItems().forEach(ci -> cartItemRepository.delete(ci));
        cart.getItems().clear();
        cartRepository.save(cart);

        return mapPurchase(purchase);
    }

    public List<PurchaseResponse> getMyPurchases(User user) {
        return purchaseRepository.findByUser(user).stream()
                .map(this::mapPurchase)
                .collect(Collectors.toList());
    }

    public List<PurchaseResponse> getAllPurchases() {
        return purchaseRepository.findAll().stream()
                .map(this::mapPurchase)
                .collect(Collectors.toList());
    }

    public PurchaseResponse getById(Long id, User requestor) {
        Purchase p = purchaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

        // Validación
        boolean isOwner = p.getUser().getId().equals(requestor.getId());
        boolean isAdmin = requestor.getRoles().stream()
                .anyMatch(r -> r.getName().name().equals("ROLE_ADMIN"));

        if (!isOwner && !isAdmin) {
            throw new RuntimeException("No autorizado para ver esta compra");
        }

        return mapPurchase(p);
    }
}




