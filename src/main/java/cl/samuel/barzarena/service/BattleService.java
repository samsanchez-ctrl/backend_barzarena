package cl.samuel.barzarena.service;

import cl.samuel.barzarena.dto.BattleRequest;
import cl.samuel.barzarena.dto.BattleResponse;
import cl.samuel.barzarena.dto.RapperResponse;
import cl.samuel.barzarena.model.*;
import cl.samuel.barzarena.repository.BattleRepository;
import cl.samuel.barzarena.repository.RapperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BattleService {

    private final BattleRepository battleRepository;
    private final RapperRepository rapperRepository;
    private final BetService betService; // NUEVO

    private RapperResponse mapRapper(Rapper r) {
        return new RapperResponse(
                r.getId(),
                r.getName(),
                r.getRealName(),
                r.getBio(),
                r.getOrigin(),
                r.getImageUrl(),
                r.getActive()
        );
    }

    private BattleResponse map(Battle b) {
        return new BattleResponse(
                b.getId(),
                mapRapper(b.getRapperA()),
                mapRapper(b.getRapperB()),
                b.getDate().toString(),
                b.getWinner(),
                b.getActive()
        );
    }

    public List<BattleResponse> getPublic() {
        return battleRepository.findByActiveTrue()
                .stream().map(this::map).collect(Collectors.toList());
    }

    public List<BattleResponse> getAdmin() {
        return battleRepository.findAll()
                .stream().map(this::map).collect(Collectors.toList());
    }

    public BattleResponse getById(Long id) {
        Battle b = battleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batalla no encontrada"));
        return map(b);
    }

    public BattleResponse create(BattleRequest req) {

        Rapper a = rapperRepository.findById(req.getRapperAId())
                .orElseThrow(() -> new RuntimeException("Rapper A no encontrado"));

        Rapper b = rapperRepository.findById(req.getRapperBId())
                .orElseThrow(() -> new RuntimeException("Rapper B no encontrado"));

        if (a.getId().equals(b.getId())) {
            throw new RuntimeException("La batalla debe tener dos raperos diferentes.");
        }

        Battle battle = new Battle();
        battle.setRapperA(a);
        battle.setRapperB(b);
        battle.setDate(LocalDateTime.parse(req.getDate()));
        battle.setWinner(Battle.BattleWinner.NONE);
        battle.setActive(req.getActive() == null ? true : req.getActive());

        return map(battleRepository.save(battle));
    }

    public BattleResponse update(Long id, BattleRequest req) {
        Battle b = battleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batalla no encontrada"));

        Rapper a = rapperRepository.findById(req.getRapperAId())
                .orElseThrow(() -> new RuntimeException("Rapper A no encontrado"));

        Rapper c = rapperRepository.findById(req.getRapperBId())
                .orElseThrow(() -> new RuntimeException("Rapper B no encontrado"));

        b.setRapperA(a);
        b.setRapperB(c);
        b.setDate(LocalDateTime.parse(req.getDate()));
        b.setActive(req.getActive());

        return map(battleRepository.save(b));
    }

    public BattleResponse setWinner(Long id, Battle.BattleWinner winner) {
        Battle b = battleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batalla no encontrada"));

        b.setWinner(winner);
        battleRepository.save(b);

        // 💥 Paga automáticamente todas las apuestas
        betService.processBetsForBattle(b);

        return map(b);
    }

    public void delete(Long id) {
        Battle b = battleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batalla no encontrada"));
        battleRepository.delete(b);
    }
}


