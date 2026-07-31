package com.example.MindConnect.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenEntity extends BaseClass{

    private String token;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private LocalDateTime expirationDate;

    private LocalDateTime createdAt;

}
