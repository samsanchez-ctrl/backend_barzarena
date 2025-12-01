package cl.samuel.barzarena.dto;

import cl.samuel.barzarena.model.Battle;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BetRequest {

    @NotNull(message = "El id de la batalla es obligatorio")
    private Long battleId;

    @NotNull(message = "La predicción es obligatoria")
    private Battle.BattleWinner prediction; // A o B

    @NotNull(message = "El monto es obligatorio")
    @Min(value = 1, message = "El monto debe ser mayor o igual a 1")
    private Double amount;
}
