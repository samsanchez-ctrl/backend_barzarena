package cl.samuel.barzarena.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bets")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Bet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Battle battle;

    @Enumerated(EnumType.STRING)
    private Battle.BattleWinner prediction; // A o B

    @Column(nullable = false)
    private Double amount;

    private boolean paid = false; // Para saber si se pagó después
}
