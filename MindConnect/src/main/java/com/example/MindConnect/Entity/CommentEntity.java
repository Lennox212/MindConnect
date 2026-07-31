package com.example.MindConnect.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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
    @ManyToOne(optional = false)
    private UserEntity commentedBy;

    @JoinColumn(name = "post_id")
    @ManyToOne(optional = false)
    private PostEntity post;

    private LocalDateTime updatedAt;

    @NotBlank
    private String content;

    private LocalDateTime commentedAt;





}
