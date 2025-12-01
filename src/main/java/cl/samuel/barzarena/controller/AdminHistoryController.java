package cl.samuel.barzarena.controller;

import cl.samuel.barzarena.dto.PurchaseResponse;
import cl.samuel.barzarena.service.AdminHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/history")
@RequiredArgsConstructor
public class AdminHistoryController {

    private final AdminHistoryService adminHistoryService;

    @GetMapping("/purchases")
    @PreAuthorize("hasRole('ADMIN')")
    public List<PurchaseResponse> getAllPurchases() {
        return adminHistoryService.getAllPurchases();
    }
}

