package cl.samuel.barzarena.controller;

import cl.samuel.barzarena.dto.PurchaseResponse;
import cl.samuel.barzarena.security.CustomUserDetailsService;
import cl.samuel.barzarena.service.UserHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class UserHistoryController {

    private final UserHistoryService userHistoryService;

    @GetMapping("/me")
    public List<PurchaseResponse> getMyPurchaseHistory(
            @AuthenticationPrincipal UserDetails details
    ) {
        return userHistoryService.getHistory(details.getUsername());
    }
}

