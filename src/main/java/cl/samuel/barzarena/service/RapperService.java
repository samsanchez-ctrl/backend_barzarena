package cl.samuel.barzarena.service;

import cl.samuel.barzarena.dto.RapperRequest;
import cl.samuel.barzarena.dto.RapperResponse;
import cl.samuel.barzarena.model.Rapper;
import cl.samuel.barzarena.repository.RapperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RapperService {

    private final RapperRepository rapperRepository;

    private RapperResponse map(Rapper r) {
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

    public List<RapperResponse> getAllPublic() {
        return rapperRepository.findByActiveTrue()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public List<RapperResponse> getAllAdmin() {
        return rapperRepository.findAll()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public RapperResponse getById(Long id) {
        Rapper r = rapperRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rapper no encontrado"));
        return map(r);
    }

    public RapperResponse create(RapperRequest req) {
        if (rapperRepository.existsByName(req.getName())) {
            throw new RuntimeException("Ya existe un rapero con ese nombre artístico");
        }

        Rapper r = new Rapper();
        r.setName(req.getName());
        r.setRealName(req.getRealName());
        r.setBio(req.getBio());
        r.setOrigin(req.getOrigin());
        r.setImageUrl(req.getImageUrl());
        r.setActive(req.getActive() == null ? true : req.getActive());

        Rapper saved = rapperRepository.save(r);
        return map(saved);
    }

    public RapperResponse update(Long id, RapperRequest req) {
        Rapper r = rapperRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rapper no encontrado"));

        if (req.getName() != null && !req.getName().equals(r.getName())) {
            if (rapperRepository.existsByName(req.getName())) {
                throw new RuntimeException("Ya existe un rapero con ese nombre artístico");
            }
            r.setName(req.getName());
        }
        if (req.getRealName() != null) r.setRealName(req.getRealName());
        if (req.getBio() != null) r.setBio(req.getBio());
        if (req.getOrigin() != null) r.setOrigin(req.getOrigin());
        if (req.getImageUrl() != null) r.setImageUrl(req.getImageUrl());
        if (req.getActive() != null) r.setActive(req.getActive());

        Rapper saved = rapperRepository.save(r);
        return map(saved);
    }

    public void delete(Long id) {
        Rapper r = rapperRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rapper no encontrado"));
        rapperRepository.delete(r);
    }
}

