package cl.samuel.barzarena.controller;

import cl.samuel.barzarena.dto.RapperRequest;
import cl.samuel.barzarena.dto.RapperResponse;
import cl.samuel.barzarena.service.RapperService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rappers")
@RequiredArgsConstructor
public class RapperController {

    private final RapperService rapperService;

    // listado público (solo raperos activos)
    @GetMapping
    public ResponseEntity<List<RapperResponse>> getAllPublic() {
        return ResponseEntity.ok(rapperService.getAllPublic());
    }

    // ver detalle público
    @GetMapping("/{id}")
    public ResponseEntity<RapperResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(rapperService.getById(id));
    }

    // listado completo (admin)
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RapperResponse>> getAllAdmin() {
        return ResponseEntity.ok(rapperService.getAllAdmin());
    }

    // crear (admin)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RapperResponse> create(@Valid @RequestBody RapperRequest req) {
        return ResponseEntity.ok(rapperService.create(req));
    }

    // actualizar (admin)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RapperResponse> update(@PathVariable Long id,
                                                 @Valid @RequestBody RapperRequest req) {
        return ResponseEntity.ok(rapperService.update(id, req));
    }

    // borrar (admin)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        rapperService.delete(id);
        return ResponseEntity.ok("Rapper eliminado");
    }
}

