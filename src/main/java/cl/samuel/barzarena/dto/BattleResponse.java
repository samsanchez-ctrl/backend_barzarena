package cl.samuel.barzarena.dto;

import cl.samuel.barzarena.model.Battle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BattleResponse {

    private Long id;
    private RapperResponse rapperA;
    private RapperResponse rapperB;
    private String date;
    private Battle.BattleWinner winner;
    private Boolean active;
}

