package cl.samuel.barzarena.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "battles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Battle {

    public enum BattleWinner {
        NONE,
        A,
        B
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Rapper A
    @ManyToOne(optional = false)
    private Rapper rapperA;

    // Rapper B
    @ManyToOne(optional = false)
    private Rapper rapperB;

    // Fecha de la batalla
    @Column(nullable = false)
    private LocalDateTime date;

    // Ganador real (para pagar apuestas)
    @Enumerated(EnumType.STRING)
    private BattleWinner winner = BattleWinner.NONE;

    // visible o no para el público
    @Column(nullable = false)
    private Boolean active = true;
}


