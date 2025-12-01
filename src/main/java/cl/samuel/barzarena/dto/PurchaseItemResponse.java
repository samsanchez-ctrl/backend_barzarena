package cl.samuel.barzarena.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PurchaseItemResponse {

    private Long productId;          // ID del producto comprado
    private String productName;      // Nombre del producto
    private int quantity;            // Cantidad comprada
    private double priceAtPurchase;  // Precio al momento de comprar
    private double subtotal;         // priceAtPurchase * quantity
}



