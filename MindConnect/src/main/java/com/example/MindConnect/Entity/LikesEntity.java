package com.example.MindConnect.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder


public class LikesEntity extends BaseClass{
    @JoinColumn(name = "liked_by_id")
    @ManyToOne
    private UserEntity likedBy;

    @JoinColumn(name = "post_liked_id")
    @ManyToOne
    private PostEntity postLiked;

    private LocalDateTime timeLiked;

}
