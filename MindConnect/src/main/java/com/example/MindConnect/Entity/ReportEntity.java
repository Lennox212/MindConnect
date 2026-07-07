package com.example.MindConnect.Entity;


import com.example.MindConnect.Enums.ReportStatus;
import com.example.MindConnect.Enums.ReportTargetType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ReportEntity extends BaseClass {
    @JoinColumn(name = "reporter_id")
    @ManyToOne
    private UserEntity reportedBy;

    @JoinColumn(name = "reported_post_id")
    @ManyToOne
    private PostEntity reportedPost;

    @JoinColumn(name = "reported_comment_id")
    @ManyToOne
    private CommentEntity reportedComment;

    private String motive;

    private LocalDateTime reportedAt;

    @JoinColumn(name = "reviewed_by")
    @ManyToOne
    private UserEntity reviewedBy;

    @JoinColumn(name = "reported_user")
    @ManyToOne
    private UserEntity reportedUser;

    @JoinColumn(name = "reported_message")
    @ManyToOne
    private MessageEntity reportedMessage;

    @JoinColumn(name = "reported_group")
    @ManyToOne
    private GroupEntity reportedGroup;

    private LocalDateTime reviewedAt;

    private String adminNote;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    @Enumerated(EnumType.STRING)
    private ReportTargetType targetType;




}
