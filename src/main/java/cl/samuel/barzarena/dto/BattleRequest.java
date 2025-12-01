package cl.samuel.barzarena.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BattleRequest {

    @NotNull(message = "El ID del rapero A es obligatorio")
    private Long rapperAId;

    @NotNull(message = "El ID del rapero B es obligatorio")
    private Long rapperBId;

    @NotNull(message = "La fecha de la batalla es obligatoria")
    private String date; // ISO string (ej: "2025-01-10T20:00")

    private Boolean active = true;
}

