package cl.samuel.barzarena.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rappers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rapper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // nombre artístico obligatorio y único
    @Column(nullable = false, unique = true)
    private String name;

    // nombre real opcional
    private String realName;

    // biografía / descripción
    @Column(length = 2000)
    private String bio;

    // país o ciudad de origen
    private String origin;

    // URL de imagen (opcional)
    private String imageUrl;

    // activo/inactivo para no borrar del todo
    @Column(nullable = false)
    private Boolean active = true;
}

