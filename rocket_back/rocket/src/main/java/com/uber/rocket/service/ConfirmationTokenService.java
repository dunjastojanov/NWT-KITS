package com.uber.rocket.service;

import com.uber.rocket.entity.user.ConfirmationToken;
import com.uber.rocket.entity.user.ConformationTokenType;
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

    private final static int FIFTEEN_MINUTES = 15;

    public void createToken(User user, String token,ConformationTokenType tokenType) {
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setCreatedAt(LocalDateTime.now());
        confirmationToken.setExpiredAt(LocalDateTime.now().plusMinutes(FIFTEEN_MINUTES));
        confirmationToken.setUser(user);
        confirmationToken.setTokenType(tokenType);
        confirmationToken.setToken(token);
        confirmationTokenRepository.save(confirmationToken);
    }

    public User validateToken(String token) {
        ConfirmationToken confirmationToken = getConfirmationToken(token);
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepository.save(confirmationToken);
        return confirmationToken.getUser();
    }

    private ConfirmationToken getConfirmationToken(String token) {
        Optional<ConfirmationToken> confirmationToken = confirmationTokenRepository.findByToken(token);
        if (confirmationToken.isEmpty()) {
            throw new RuntimeException("Token doesn't exist");
        }
        String tokenTypeString = confirmationToken.get().getTokenType().equals(ConformationTokenType.REGISTRATION_CONFORMATION_TOKEN) ? "Registration" : "Password";
        if (confirmationToken.get().getConfirmedAt() != null) {
            throw new RuntimeException(tokenTypeString + " token is already confirmed");
        }
        if (confirmationToken.get().getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException(tokenTypeString + " token has already expired");
        }
        return confirmationToken.get();
    }
}
