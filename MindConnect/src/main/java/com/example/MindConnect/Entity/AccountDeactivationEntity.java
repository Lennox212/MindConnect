package com.example.MindConnect.Entity;

import com.example.MindConnect.Enums.AccountStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDeactivationEntity extends BaseClass{

    private String reason;

    private LocalDateTime deactivatedAt;

    private LocalDateTime acttivatedAt;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private UserEntity user;

    private AccountStatus status;
}
