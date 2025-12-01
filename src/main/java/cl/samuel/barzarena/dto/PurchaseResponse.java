package cl.samuel.barzarena.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PurchaseResponse {
    private Long id;
    private Double total;
    private LocalDateTime date;
    private List<PurchaseItemResponse> items;
}


