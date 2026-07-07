package com.example.MindConnect.Repository;

import com.example.MindConnect.Entity.AccountDeactivationEntity;
import com.example.MindConnect.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountDeactivationRepository extends JpaRepository<AccountDeactivationEntity, UUID> {
}
