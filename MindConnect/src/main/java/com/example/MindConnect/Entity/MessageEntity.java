package com.example.MindConnect.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder


public class MessageEntity extends BaseClass{
    @OneToMany(mappedBy = "reportedMessage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReportEntity> reports;


    @ManyToOne
    @JoinColumn(name = "sender_id")
    private UserEntity sender;

    @JoinColumn(name = "recipient_id")
    @ManyToOne
    private UserEntity recipient;

    private String message;

    private LocalDateTime sentAt;

    private boolean isRead;

    private LocalDateTime readAt;

    private LocalDateTime updatedAt;

    private boolean edited;

    @Builder.Default
    private boolean deletedBySender = false;

    @Builder.Default
    private boolean deletedByRecipient = false;


}
