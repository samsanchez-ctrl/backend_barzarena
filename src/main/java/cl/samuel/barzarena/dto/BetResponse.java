package cl.samuel.barzarena.dto;

import cl.samuel.barzarena.model.Battle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BetResponse {

    private Long id;
    private String username;
    private Long battleId;
    private String battleDate;
    private Battle.BattleWinner prediction;
    private Double amount;
    private boolean paid;
}

