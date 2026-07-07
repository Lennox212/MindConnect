package com.example.MindConnect.Entity;

import com.example.MindConnect.Enums.AccountStatus;
import com.example.MindConnect.Enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder


public class UserEntity extends BaseClass{
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY) //cascade = if changes is made, change both entities fetch = LAZY - wait to be called to perform your action. 2 types - eager or lazy. Eager - load info faster
    private List<MessageEntity> senders = new ArrayList<>(); //linking entities(relationships)

    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MessageEntity> recipients = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AccountDeactivationEntity> deactivatedUsers = new ArrayList<>();

    @OneToMany(mappedBy = "reportedBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReportEntity> reportsSubmitted = new ArrayList<>();

    @OneToMany(mappedBy = "reportedUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReportEntity> reportsReceived = new ArrayList<>();

    @OneToMany(mappedBy = "reviewedBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReportEntity> reportsReviewed = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostEntity> postAuthors = new ArrayList<>();

    @OneToMany(mappedBy = "likedBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LikesEntity> likes = new ArrayList<>();

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GroupEntity> owners = new ArrayList<>();

    @ManyToMany(mappedBy = "members", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GroupEntity> groupMembers = new ArrayList<>();

    @OneToMany(mappedBy = "commentedBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CommentEntity> comments = new ArrayList<>();

    @OneToMany(mappedBy = "blockedUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BlockedEntity> blocked_users = new ArrayList<>();

    @OneToMany(mappedBy = "blockedBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BlockedEntity> blocked_by_me = new ArrayList<>();




    @NotBlank
    private String firstName;


    @NotBlank
    private String lastName;

    @NotBlank
    @Size(min = 8)
    private String password;

    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    private String gender;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime signUpDate;

    private String mentalCondition;




// abstract parent class - get repeated methods from it
//do i want to save the user information in my DB? == ENTITY


}
