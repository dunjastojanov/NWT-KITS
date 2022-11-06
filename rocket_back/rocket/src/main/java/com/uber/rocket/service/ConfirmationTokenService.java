package com.uber.rocket.service;

import com.uber.rocket.entity.user.ConfirmationToken;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.repository.ConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ConfirmationTokenService {
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    //    @Autowired
//    private UserService userService;
    private final static int FIFTEEN_MINUTES = 15;

    public void createToken(User user, String token) {
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setCreatedAt(LocalDateTime.now());
        confirmationToken.setExpiredAt(LocalDateTime.now().plusMinutes(FIFTEEN_MINUTES));
        confirmationToken.setUser(user);
        confirmationToken.setToken(token);
        confirmationTokenRepository.save(confirmationToken);
    }

    public User validateToken(String token) {
        Optional<ConfirmationToken> confirmationToken = confirmationTokenRepository.findByToken(token);
        if (confirmationToken.isEmpty()) {
            throw new RuntimeException("Registration token doesn't exist");
        }
        if (confirmationToken.get().getConfirmedAt() != null) {
            throw new RuntimeException("Registration token is already confirmed");
        }
        if (confirmationToken.get().getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Registration token is already confirmed");
        }
        confirmationToken.get().setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepository.save(confirmationToken.get());
        return confirmationToken.get().getUser();
    }
}
