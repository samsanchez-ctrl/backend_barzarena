package cl.samuel.barzarena.service;

import cl.samuel.barzarena.dto.CartItemResponse;
import cl.samuel.barzarena.dto.CartResponse;
import cl.samuel.barzarena.model.Cart;
import cl.samuel.barzarena.model.CartItem;
import cl.samuel.barzarena.model.Product;
import cl.samuel.barzarena.model.User;
import cl.samuel.barzarena.repository.CartItemRepository;
import cl.samuel.barzarena.repository.CartRepository;
import cl.samuel.barzarena.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    private CartItemResponse mapItem(CartItem ci) {
        return new CartItemResponse(
                ci.getId(),
                ci.getProduct().getId(),
                ci.getProduct().getName(),
                ci.getProduct().getPrice(),
                ci.getQuantity(),
                ci.getProduct().getPrice() * ci.getQuantity()
        );
    }

    private CartResponse map(Cart cart) {
        List<CartItemResponse> items = cart.getItems().stream()
                .map(this::mapItem)
                .collect(Collectors.toList());

        Double total = items.stream()
                .mapToDouble(CartItemResponse::getSubtotal)
                .sum();

        return new CartResponse(cart.getId(), items, total);
    }

    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart c = new Cart();
            c.setUser(user);
            return cartRepository.save(c);
        });
    }

    public CartResponse getCart(User user) {
        return map(getOrCreateCart(user));
    }

    public CartResponse addItem(User user, Long productId, int qty) {
        if (qty <= 0) throw new RuntimeException("La cantidad debe ser mayor a 0");

        Cart cart = getOrCreateCart(user);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (product.getStock() < qty)
            throw new RuntimeException("Stock insuficiente");

        CartItem existing = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            int newQty = existing.getQuantity() + qty;
            if (product.getStock() < newQty)
                throw new RuntimeException("Stock insuficiente");

            existing.setQuantity(newQty);
            cartItemRepository.save(existing);
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(qty);
            // persistimos el item y lo agregamos a la lista del carrito
            cartItemRepository.save(item);
            cart.getItems().add(item);
        }

        Cart saved = cartRepository.save(cart);
        return map(saved);
    }

    public CartResponse updateItem(User user, Long itemId, int qty) {
        if (qty <= 0) throw new RuntimeException("La cantidad debe ser mayor a 0");

        Cart cart = getOrCreateCart(user);

        CartItem item = cart.getItems().stream()
                .filter(ci -> ci.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item no encontrado en tu carrito"));

        Product product = item.getProduct();

        if (product.getStock() < qty)
            throw new RuntimeException("Stock insuficiente");

        item.setQuantity(qty);
        cartItemRepository.save(item);

        Cart saved = cartRepository.save(cart);
        return map(saved);
    }

    public CartResponse removeItem(User user, Long itemId) {
        Cart cart = getOrCreateCart(user);

        CartItem item = cart.getItems().stream()
                .filter(ci -> ci.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item no encontrado en tu carrito"));

        cart.getItems().remove(item);
        cartItemRepository.delete(item);

        Cart saved = cartRepository.save(cart);
        return map(saved);
    }

    public CartResponse clearCart(User user) {
        Cart cart = getOrCreateCart(user);
        // elimina los items de la base y de la colección
        cart.getItems().forEach(ci -> cartItemRepository.delete(ci));
        cart.getItems().clear();
        cartRepository.save(cart);
        return map(cart);
    }
}


