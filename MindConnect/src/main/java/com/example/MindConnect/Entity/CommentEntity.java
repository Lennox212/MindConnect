package com.example.MindConnect.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CommentEntity extends BaseClass{
    @OneToMany(mappedBy = "reportedComment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReportEntity> reports;



    @JoinColumn(name = "commented_by_id")
    @ManyToOne
    private UserEntity commentedBy;

    @JoinColumn(name = "post_id")
    @ManyToOne
    private PostEntity post;

    private LocalDateTime updatedAt;

    private String comment;

    private LocalDateTime commentedAt;





}
