package cl.samuel.barzarena.repository;

import cl.samuel.barzarena.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByRut(String rut);

    Boolean existsByPhone(String phone);
}

