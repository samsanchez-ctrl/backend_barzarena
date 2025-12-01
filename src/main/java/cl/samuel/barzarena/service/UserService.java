package cl.samuel.barzarena.service;

import cl.samuel.barzarena.model.User;
import cl.samuel.barzarena.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User addBalance(User user, Double amount) {
        if (amount <= 0) {
            throw new RuntimeException("El monto debe ser mayor a 0");
        }

        user.setSaldo(user.getSaldo() + amount);
        return userRepository.save(user);
    }

    public User spendBalance(User user, Double amount) {
        if (amount <= 0) {
            throw new RuntimeException("El monto debe ser mayor a 0");
        }

        if (user.getSaldo() < amount) {
            throw new RuntimeException("Saldo insuficiente");
        }

        user.setSaldo(user.getSaldo() - amount);
        return userRepository.save(user);
    }
}
