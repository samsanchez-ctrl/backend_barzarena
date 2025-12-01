package cl.samuel.barzarena.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @Email(message = "Correo inválido")
    @NotBlank(message = "El correo es obligatorio")
    private String email;

    @NotBlank(message = "El rut es obligatorio")
    @Pattern(regexp = "^\\d{7,8}[0-9Kk]$", message = "Formato de RUT inválido (sin puntos ni guion)")
    private String rut;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^(\\+?56)?[2-9]\\d{7}$", message = "Teléfono inválido")
    private String phone;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha debe ser en el pasado")
    private java.time.LocalDate birthDate;
}


