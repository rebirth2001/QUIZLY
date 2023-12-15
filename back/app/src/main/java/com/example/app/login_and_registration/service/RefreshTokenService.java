package com.example.app.login_and_registration.service;

import com.example.app.login_and_registration.model.RefreshToken;
import com.example.app.login_and_registration.repository.RefreshTokenRepository;
import com.example.app.login_and_registration.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${jwt.refresh.expiration}")
    private Long expiration;

    @Autowired
    private RefreshTokenRepository refTokRepo;

    @Autowired
    private UserRepository userRepository;

    public Optional<RefreshToken> findByToken(String token){
        return refTokRepo.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(expiration));
        refreshToken.setToken(UUID.randomUUID().toString());
        refTokRepo.save(refreshToken);
        return refreshToken;
    }
    public Boolean isExpired(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refTokRepo.delete(token);
            return true;
        }
        return false;
    }

    @Transactional
    public int deleteByUserId(Long userId) {
        return refTokRepo.deleteByUser(userRepository.findById(userId).get());
    }
}
