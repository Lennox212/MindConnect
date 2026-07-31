package com.example.MindConnect.Service;

import com.example.MindConnect.Config.JwtGenerator;
import com.example.MindConnect.CustomExceptions.TokenExpiredException;
import com.example.MindConnect.CustomExceptions.TokenNotFoundException;
import com.example.MindConnect.Entity.RefreshTokenEntity;
import com.example.MindConnect.Entity.UserEntity;
import com.example.MindConnect.Repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor

public class RefreshTokenServiceImpl {
    @Value("${app.jwt-refresh-expiration}")
    private Long jwtRefreshExpiration;

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtGenerator jwtGenerator;

    public RefreshTokenEntity createRefreshToken(UserEntity user){


    Optional<RefreshTokenEntity> existingToken = refreshTokenRepository.findByUser(user);

    existingToken.ifPresent(refreshTokenRepository::delete); //method referencing -shorter way to writing lambadas

    String refreshToken = jwtGenerator.generateRefreshToken(user);



    RefreshTokenEntity tokenEntity = RefreshTokenEntity.builder()
            .user(user)
            .createdAt(LocalDateTime.now())
            .expirationDate(LocalDateTime.now().plusSeconds(jwtRefreshExpiration/1000))
            .token(refreshToken)
            .build();

    return refreshTokenRepository.save(tokenEntity);
    }

    public RefreshTokenEntity validateRefreshToken(String token){

        jwtGenerator.validateToken(token); //validate just to make sure users are who they say they are

        RefreshTokenEntity refreshToken =  refreshTokenRepository.findByToken(token).
                orElseThrow(()->new TokenNotFoundException("Token not found"));


        if(refreshToken.getExpirationDate().isBefore(LocalDateTime.now())){ //expiration < time right now ? expired : not expired
            refreshTokenRepository.delete(refreshToken);
            throw new TokenExpiredException("Token expired");
        }

        return refreshToken;
    }

    public void deleteRefreshToken(String token) {

        RefreshTokenEntity refreshToken =
                refreshTokenRepository.findByToken(token)
                        .orElseThrow(() ->
                                new TokenNotFoundException("Refresh token not found"));

        refreshTokenRepository.delete(refreshToken);
    }



}
