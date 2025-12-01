package cl.samuel.barzarena.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Obligatorio y único
    @Column(nullable = false, unique = true)
    private String username;

    // Hash de contraseña
    @Column(nullable = false)
    private String password;

    // Correo único y obligatorio
    @Column(nullable = false, unique = true)
    private String email;

    // Nuevo: rut del usuario (sin puntos ni guion)
    @Column(nullable = false, unique = true)
    private String rut;

    // Nuevo: telefono (+569... o 9 dígitos)
    @Column(nullable = false)
    private String phone;

    // Nuevo: fecha de nacimiento (mayor de 18 años -> se validará en DTO)
    @Column(nullable = false)
    private LocalDate birthDate;

    // Saldo del usuario
    private Double saldo = 0.0;

    // Roles
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // Auditoría automática
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}