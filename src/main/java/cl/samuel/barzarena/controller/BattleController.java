package cl.samuel.barzarena.controller;

import cl.samuel.barzarena.dto.BattleRequest;
import cl.samuel.barzarena.dto.BattleResponse;
import cl.samuel.barzarena.model.Battle;
import cl.samuel.barzarena.service.BattleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/battles")
@RequiredArgsConstructor
public class BattleController {

    private final BattleService battleService;

    // público
    @GetMapping
    public ResponseEntity<List<BattleResponse>> getPublic() {
        return ResponseEntity.ok(battleService.getPublic());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BattleResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(battleService.getById(id));
    }

    // admin: ver todo
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BattleResponse>> getAdmin() {
        return ResponseEntity.ok(battleService.getAdmin());
    }

    // admin: crear
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BattleResponse> create(@Valid @RequestBody BattleRequest req) {
        return ResponseEntity.ok(battleService.create(req));
    }

    // admin: editar
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BattleResponse> update(@PathVariable Long id,
                                                 @Valid @RequestBody BattleRequest req) {
        return ResponseEntity.ok(battleService.update(id, req));
    }

    // admin: asignar ganador
    @PostMapping("/{id}/winner")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BattleResponse> setWinner(
            @PathVariable Long id,
            @RequestParam Battle.BattleWinner winner
    ) {
        return ResponseEntity.ok(battleService.setWinner(id, winner));
    }

    // admin: borrar
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        battleService.delete(id);
        return ResponseEntity.ok("Batalla eliminada");
    }
}

