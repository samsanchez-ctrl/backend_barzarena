package cl.samuel.barzarena.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserHistoryResponse {

    private String username;
    private Double saldoActual;

    private List<BetResponse> apuestas;
    private List<PurchaseResponse> compras;
}

