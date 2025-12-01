package cl.samuel.barzarena.repository;


import cl.samuel.barzarena.model.Role;
import cl.samuel.barzarena.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
