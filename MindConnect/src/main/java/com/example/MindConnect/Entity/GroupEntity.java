package com.example.MindConnect.Entity;


import com.example.MindConnect.Enums.GroupVisibility;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder

public class GroupEntity extends BaseClass{


    @Column(unique = true)
    private String groupName;

    private String description;

    @JoinColumn(name = "owner_id")
    @ManyToOne
    private UserEntity owner;

    @ManyToMany
    private Set<UserEntity> members;

    @OneToMany(mappedBy = "reportedGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReportEntity> reports;

    @Enumerated(EnumType.STRING)
    private GroupVisibility groupVisibility;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
