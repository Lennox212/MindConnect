package com.example.MindConnect.Repository;

import com.example.MindConnect.Entity.GroupEntity;
import com.example.MindConnect.Enums.GroupVisibility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GroupsRepository extends JpaRepository<GroupEntity, UUID> {

boolean existsByGroupName(String name);
List<GroupEntity> findByGroupVisibility(GroupVisibility visibility);

}
