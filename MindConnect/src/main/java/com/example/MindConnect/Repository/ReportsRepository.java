package com.example.MindConnect.Repository;

import com.example.MindConnect.Entity.*;
import com.example.MindConnect.Enums.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.UUID;

public interface ReportsRepository extends JpaRepository<ReportEntity, UUID> {

    boolean existsByReportedByAndReportedPost(UserEntity user, PostEntity post);
    boolean existsByReportedByAndReportedComment(UserEntity user, CommentEntity comment);
    boolean existsByReportedByAndReportedUser(UserEntity user, UserEntity reportedUser);
    boolean existsByReportedByAndReportedMessage(UserEntity user, MessageEntity message);
    boolean existsByReportedByAndReportedGroup(UserEntity user, GroupEntity group);
    List<ReportEntity> findByStatus(ReportStatus status);
}
