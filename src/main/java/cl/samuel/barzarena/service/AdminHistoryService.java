package cl.samuel.barzarena.service;

import cl.samuel.barzarena.dto.PurchaseResponse;
import cl.samuel.barzarena.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminHistoryService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseService purchaseService;

    public List<PurchaseResponse> getAllPurchases() {

        return purchaseRepository.findAll()
                .stream()
                .map(purchaseService::mapPurchase)
                .toList();
    }
}


