package cl.samuel.barzarena.service;

import cl.samuel.barzarena.dto.PurchaseResponse;
import cl.samuel.barzarena.model.User;
import cl.samuel.barzarena.repository.PurchaseRepository;
import cl.samuel.barzarena.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserHistoryService {

    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final PurchaseService purchaseService;

    public List<PurchaseResponse> getHistory(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return purchaseRepository.findByUser(user)
                .stream()
                .map(purchaseService::mapPurchase)
                .toList();
    }
}


