package cl.samuel.barzarena.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RapperRequest {

    @NotBlank(message = "El nombre artístico es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String name;

    @Size(max = 100, message = "El nombre real no puede exceder 100 caracteres")
    private String realName;

    @Size(max = 2000, message = "La biografía no puede exceder 2000 caracteres")
    private String bio;

    @Size(max = 100, message = "Origen no puede exceder 100 caracteres")
    private String origin;

    private String imageUrl;

    // si quieres crear como inactivo por defecto, puedes poner false
    private Boolean active = true;
}

