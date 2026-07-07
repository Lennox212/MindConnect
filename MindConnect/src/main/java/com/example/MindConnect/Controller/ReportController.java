package com.example.MindConnect.Controller;

import com.example.MindConnect.Payload.Request.ReportRequest.*;
import com.example.MindConnect.Payload.Response.ReportResponse.*;
import com.example.MindConnect.Service.ReportServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportServiceImpl reportService;

    @PostMapping("/report-post")
    public ReportPostResponse reportPost(@RequestBody ReportPostRequest request){
        return reportService.reportPost(request);
    }

    @PostMapping("/report-comment")
    public ReportCommentResponse reportComment(@RequestBody ReportCommentRequest request){

        return reportService.reportComment(request);
    }

    @PostMapping("/report-message")
    public ReportMessageResponse reportMessage(@RequestBody ReportMessageRequest request){
        return reportService.reportMessage(request);
    }

    @PostMapping("/report-group")
    public ReportGroupResponse reportGroup(@RequestBody ReportGroupRequest request){
        return reportService.reportGroup(request);
    }

    @PostMapping("/report-user")
    public ReportUserResponse reportUser(@RequestBody ReportUserRequest request){
        return reportService.reportUser(request);
    }


    @GetMapping("/get-pending-reports")
    public List<GetStatusResponse> getPendingReports(){
        return reportService.getPendingReports();
    }

    @PutMapping("/review-reports")
    public ReviewReportResponse reviewReport(@RequestBody ReviewReportRequest request){

        return reportService.reviewReport(request);
    }



}
