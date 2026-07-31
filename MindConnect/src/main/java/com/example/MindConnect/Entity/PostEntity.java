package com.example.MindConnect.Entity;


import com.example.MindConnect.Enums.PostVisibility;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder



public class PostEntity extends BaseClass{

    @OneToMany(mappedBy = "postLiked", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LikesEntity> likes = new ArrayList<>();

    //OOP principle - ASSOCIATION - call them as a field and post entity will be able to access everything inside of it
//Able to use properties of teh class you are referring

    //COMPOSITION



    @OneToMany(mappedBy = "reportedPost", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReportEntity> reports = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CommentEntity> comments = new ArrayList<>();



    @JoinColumn(name = "author_id")
    @ManyToOne(optional = false)
    private UserEntity author;

    private String content;

    private LocalDateTime postedAt;

    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private PostVisibility visibility;


}
