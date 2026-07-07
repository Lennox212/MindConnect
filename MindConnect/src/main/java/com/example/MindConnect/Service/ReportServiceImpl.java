package com.example.MindConnect.Service;

import com.example.MindConnect.Entity.*;
import com.example.MindConnect.Enums.AccountStatus;
import com.example.MindConnect.Enums.ReportStatus;
import com.example.MindConnect.Enums.ReportTargetType;
import com.example.MindConnect.Payload.Request.ReportRequest.*;
import com.example.MindConnect.Payload.Response.ReportResponse.*;
import com.example.MindConnect.Repository.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Data
@RequiredArgsConstructor

public class ReportServiceImpl {

    private final ReportsRepository reportsRepository;

    private final MessageRepository messageRepository;

    private final GroupsRepository groupsRepository;

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final CommentsRepository commentsRepository;

    //TODO--IMPLEMENT CUSTOM EXCEPTIONS


    public ReportPostResponse reportPost(ReportPostRequest request){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("Authenticated user not found"));

        if(user.getStatus() != AccountStatus.ACTIVE){
            throw new RuntimeException("You are not allowed to report this post");
        }

        PostEntity post = postRepository.findById(request.getPostID())
                .orElseThrow(()-> new RuntimeException("Post not found"));

        if(post.getAuthor().getEmail().equals(email)){

            throw new RuntimeException("You are not allowed to report your own post");
        }

        if(request.getReason() == null || request.getReason().isBlank()){
            throw new RuntimeException("Reason cannot be empty");
        }

        if(reportsRepository.existsByReportedByAndReportedPost(user, post)){
            throw new RuntimeException("You have already reported this post");
        }

        ReportEntity report = ReportEntity.builder()
                .reportedBy(user)
                .reportedPost(post)
                .motive(request.getReason())
                .reportedAt(LocalDateTime.now())
                .targetType(ReportTargetType.POST)
                .status(ReportStatus.PENDING)
                .build();


        ReportEntity saved_report = reportsRepository.save(report);

        ReportPostResponse response = ReportPostResponse.builder()
                .reportID(saved_report.getId())
                .postID(saved_report.getReportedPost().getId())
                .reportedBy(saved_report.getReportedBy().getFirstName() + " " + saved_report.getReportedBy().getLastName())
                .reason(saved_report.getMotive())
                .status(saved_report.getStatus())
                .message("Post Reported successfully")
                .build();

        return response;
    }

    public ReportCommentResponse reportComment(ReportCommentRequest request){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("Authenticated user not found"));

        if(user.getStatus() != AccountStatus.ACTIVE){

            throw new RuntimeException("You cannot report comments at this time");
        }

        CommentEntity comment = commentsRepository.findById(request.getCommentID())
                .orElseThrow(()->new RuntimeException("Comment not found"));

        if(comment.getCommentedBy().getEmail().equals(email)){
            throw new RuntimeException("You cannot report your own comment");
        }

        if(request.getReason() == null || request.getReason().isBlank()){
            throw new RuntimeException("Reason must not be blank");
        }

        if(reportsRepository.existsByReportedByAndReportedComment(user, comment)){
            throw new RuntimeException("Report has already been submitted");
        }

        ReportEntity report = ReportEntity.builder()
                .reportedBy(user)
                .reportedComment(comment)
                .motive(request.getReason())
                .reportedAt(LocalDateTime.now())
                .targetType(ReportTargetType.COMMENT)
                .status(ReportStatus.PENDING)
                .build();

        ReportEntity comment_report = reportsRepository.save(report);

        ReportCommentResponse response = ReportCommentResponse.builder()
                .reportId(comment_report.getId())
                .commentID(comment_report.getReportedComment().getId())
                .reportedBy(comment_report.getReportedBy().getFirstName() + " " + comment_report.getReportedBy().getLastName())
                .reason(comment_report.getMotive())
                .reportedAt(comment_report.getReportedAt())
                .status(comment_report.getStatus())
                .message("Comment was reported successfully")
                .build();

        return response;




    }

    public ReportMessageResponse reportMessage(ReportMessageRequest request){


        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("Authenticated user not found"));

        MessageEntity message = messageRepository.findById(request.getMessageID())
                .orElseThrow(()->new RuntimeException("Message not found"));

        if(user.getStatus() != AccountStatus.ACTIVE){
            throw new RuntimeException("You are not allowed to report messages at this time");
        }

        if(message.getSender().getEmail().equals(email)){
            throw new RuntimeException("You cannot report your own message");

        }

        if(reportsRepository.existsByReportedByAndReportedMessage(user,message)){
            throw new RuntimeException("Report has already been submitted");
        }

        if(request.getReason() == null || request.getReason().isBlank()){

            throw new RuntimeException("Reason cannot be empty");
        }

        ReportEntity report = ReportEntity.builder()
                .reportedBy(user)
                .reportedMessage(message)
                .targetType(ReportTargetType.MESSAGE)
                .motive(request.getReason())
                .reportedAt(LocalDateTime.now())
                .status(ReportStatus.PENDING)
                .build();

        ReportEntity saved_report = reportsRepository.save(report);

        ReportMessageResponse response = ReportMessageResponse.builder()
                .reportID(saved_report.getId())
                .messageID(saved_report.getReportedMessage().getId())
                .reportedBy(saved_report.getReportedBy().getFirstName() + " " + saved_report.getReportedBy().getLastName())
                .reason(saved_report.getMotive())
                .reportedAt(saved_report.getReportedAt())
                .status(saved_report.getStatus())
                .message("Message was reported successfully")
                .build();

        return response;




    }

    public ReportGroupResponse reportGroup(ReportGroupRequest request){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("Authenticated user not found"));

        if(user.getStatus() != AccountStatus.ACTIVE){
            throw new RuntimeException("You are not allowed to report groups at this time");
        }

        GroupEntity  group = groupsRepository.findById(request.getGroupID())
                .orElseThrow(()->new RuntimeException("Group not found"));

        if(request.getReason() == null || request.getReason().isBlank()){

            throw new RuntimeException("Reason must not be blank");
        }

        if(group.getOwner().getEmail().equals(email)){
            throw new RuntimeException("You cannot report your own group");
        }

        if(reportsRepository.existsByReportedByAndReportedGroup(user,group)){

            throw new RuntimeException("You have already submitted a report");
        }


        ReportEntity report = ReportEntity.builder()
                .reportedBy(user)
                .reportedGroup(group)
                .targetType(ReportTargetType.GROUP)
                .motive(request.getReason())
                .reportedAt(LocalDateTime.now())
                .status(ReportStatus.PENDING)
                .build();

        ReportEntity saved_report = reportsRepository.save(report);

        ReportGroupResponse response = ReportGroupResponse.builder()
                .reportID(saved_report.getId())
                .groupID(saved_report.getReportedGroup().getId())
                .reportedBy(saved_report.getReportedBy().getFirstName() + " " + saved_report.getReportedBy().getLastName())
                .reason(saved_report.getMotive())
                .reportedAt(saved_report.getReportedAt())
                .status(saved_report.getStatus())
                .message("Group has been reported successfully")
                .build();

        return response;


    }

    public ReportUserResponse reportUser(ReportUserRequest request){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("Authenticated user not found"));

        if(user.getStatus() != AccountStatus.ACTIVE){
            throw new RuntimeException("You are not allowed to report this user at this time");

        }

        UserEntity reported_user = userRepository.findById(request.getUserID())
                .orElseThrow(()->new RuntimeException("User not found"));

        if(user.getEmail().equals(reported_user.getEmail())){

            throw new RuntimeException("You cannot report yourself");
        }

        if(request.getReason() == null || request.getReason().isBlank()){
            throw new RuntimeException("Reason must not be blank");
        }

        if(reportsRepository.existsByReportedByAndReportedUser(user, reported_user)){
            throw new RuntimeException("Report has already been submitted");
        }

        ReportEntity report = ReportEntity.builder()
                .reportedBy(user)
                .reportedUser(reported_user)
                .targetType(ReportTargetType.USER)
                .motive(request.getReason())
                .reportedAt(LocalDateTime.now())
                .status(ReportStatus.PENDING)
                .build();

        ReportEntity saved_report = reportsRepository.save(report);

        ReportUserResponse response = ReportUserResponse.builder()
                .reportID(saved_report.getId())
                .userID(saved_report.getReportedUser().getId())
                .reportedBy(saved_report.getReportedBy().getFirstName() + " " + saved_report.getReportedBy().getLastName())
                .reason(saved_report.getMotive())
                .reportedAt(saved_report.getReportedAt())
                .status(saved_report.getStatus())
                .message("User was successfully reported")
                .build();

        return response;




    }

    public List<GetStatusResponse> getPendingReports(){

        List<ReportEntity> reports = reportsRepository.findByStatus(ReportStatus.PENDING);

        List<GetStatusResponse> pendingReports = new ArrayList<>();

       for(ReportEntity report: reports){

           UUID reportedPostID = null;
           UUID reportedCommentID = null;
           UUID reportedUserID = null;
           UUID reportedGroupID = null;
           UUID reportedMessageID = null;


           if(report.getTargetType() == ReportTargetType.POST){
               reportedPostID = report.getReportedPost().getId();
           }
           else if(report.getTargetType() == ReportTargetType.COMMENT){
               reportedCommentID = report.getReportedComment().getId();
           }
           else if(report.getTargetType() == ReportTargetType.MESSAGE){
               reportedMessageID = report.getReportedMessage().getId();
           }

           else if(report.getTargetType() == ReportTargetType.USER){
               reportedUserID = report.getReportedUser().getId();
           }
           else if(report.getTargetType() == ReportTargetType.GROUP){
               reportedGroupID = report.getReportedGroup().getId();
           }


           GetStatusResponse response = GetStatusResponse.builder()
                   .reportID(report.getId())
                   .reportedBy(report.getReportedBy().getFirstName() + " " + report.getReportedBy().getLastName())
                   .reportedPostID(reportedPostID)
                   .reportedGroupID(reportedGroupID)
                   .reportedUserID(reportedUserID)
                   .reportedMessageID(reportedMessageID)
                   .reportedCommentID(reportedCommentID)
                   .reason(report.getMotive())
                   .reportedAt(report.getReportedAt())
                   .status(report.getStatus())
                   .build();

           pendingReports.add(response);

       }
       return pendingReports;
    }


public ReviewReportResponse reviewReport(ReviewReportRequest request){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("Authenticated user not found"));

        ReportEntity report = reportsRepository.findById(request.getReportID())
                .orElseThrow(()->new RuntimeException("Report not found"));

        if(report.getStatus() != ReportStatus.PENDING){

            throw new RuntimeException("Case has already been reviewed");
        }

        if(request.getStatus() == null){
            throw new RuntimeException("Status is required");
        }

        if(request.getStatus() == ReportStatus.PENDING){
            throw new RuntimeException("Status cannot remain pending");
        }


        report.setReviewedAt(LocalDateTime.now());
        report.setReviewedBy(user);
        report.setAdminNote(request.getAdminNote());
        report.setStatus(request.getStatus());

        ReportEntity saved_report = reportsRepository.save(report);


        ReviewReportResponse response = ReviewReportResponse.builder()
                .reportID(saved_report.getId())
                .reviewedBy(saved_report.getReviewedBy().getFirstName() + " " + saved_report.getReviewedBy().getLastName())
                .reviewedAt(saved_report.getReviewedAt())
                .adminNote(saved_report.getAdminNote())
                .status(saved_report.getStatus())
                .message("Case has been reviewed")
                .build();

        return response;




}
















}
