package com.example.app.login_and_registration.service;

import com.example.app.login_and_registration.model.RefreshToken;
import com.example.app.login_and_registration.model.User;
import com.example.app.login_and_registration.repository.RefreshTokenRepository;
import com.example.app.login_and_registration.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
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

    public RefreshToken getOrCreateRefreshToken(User user){
        var token = refTokRepo.findByUser(user);
        if(token.isPresent() && !isExpired(token.get())){
            return token.get();
        }
        return createRefreshToken(user);
    }
    public RefreshToken createRefreshToken(User user){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
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
        var user = userRepository.findById(userId);
        return user.map(value -> refTokRepo.deleteByUser(value)).orElse(0);
    }
}
