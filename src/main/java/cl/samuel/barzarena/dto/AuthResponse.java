package cl.samuel.barzarena.dto;

import cl.samuel.barzarena.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String username;
    private Set<Role> roles;
    private Double saldo;
}

