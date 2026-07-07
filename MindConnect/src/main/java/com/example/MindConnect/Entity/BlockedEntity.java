package com.example.MindConnect.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlockedEntity  extends BaseClass{

    @JoinColumn(name = "blocked_user_id")
    @ManyToOne
    private UserEntity blockedUser;

    @JoinColumn(name = "blocked_by_id")
    @ManyToOne
    private UserEntity blockedBy;

    private LocalDateTime blockedAt;

    private String motive;




}
