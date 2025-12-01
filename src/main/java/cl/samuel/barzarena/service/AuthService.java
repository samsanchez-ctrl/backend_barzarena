package cl.samuel.barzarena.service;

import cl.samuel.barzarena.dto.RegisterRequest;
import cl.samuel.barzarena.model.Role;
import cl.samuel.barzarena.model.RoleName;
import cl.samuel.barzarena.model.User;
import cl.samuel.barzarena.repository.RoleRepository;
import cl.samuel.barzarena.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    // ===========================
    //        REGISTRO
    // ===========================
    public void register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso.");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado.");
        }

        // Validación edad mínima
        int edad = java.time.Period.between(request.getBirthDate(), java.time.LocalDate.now()).getYears();
        if (edad < 18) {
            throw new RuntimeException("Debes ser mayor de 18 años para registrarte.");
        }

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("No se encontró ROLE_USER"));

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRut(request.getRut());
        newUser.setPhone(request.getPhone());
        newUser.setBirthDate(request.getBirthDate());
        newUser.getRoles().add(userRole);
        newUser.setSaldo(0.0);

        userRepository.save(newUser);
    }

}




