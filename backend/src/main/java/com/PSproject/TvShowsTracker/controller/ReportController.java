package com.PSproject.TvShowsTracker.controller;

import com.PSproject.TvShowsTracker.dto.NewReportDto;
import com.PSproject.TvShowsTracker.service.ReportService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/TST/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @ApiOperation(value = "Create a new report")
    @PostMapping("/add")
    public ResponseEntity createReport(@ApiParam(value = "Requires a report body") @RequestBody @Valid NewReportDto newReport) {
        reportService.createReport(newReport);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Return all reports")
    @GetMapping("/view-all")
    public ResponseEntity viewReports() {
        return ResponseEntity.ok().body(reportService.fetchReports(false));
    }

    @ApiOperation(value = "Return all unread reports")
    @GetMapping("/view-not-viewed")
    public ResponseEntity viewFirstReports() {
        return ResponseEntity.ok().body(reportService.fetchReports(true));
    }

    @ApiOperation(value = "Delete a report")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteReport(@ApiParam(value = "Requires a report id") @PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Delete a report")
    @DeleteMapping("/delete-by-comment/{id}")
    public ResponseEntity deleteReportByComment(@ApiParam(value = "Requires a comment id") @PathVariable Long id) {
        reportService.deleteAllReport(reportService.findReportByComment(id));
        return ResponseEntity.ok().build();
    }

        @ApiOperation(value = "Return a report")
    @GetMapping("/{id}")
    public ResponseEntity viewReport(@ApiParam(value = "Requires a report id") @PathVariable Long id) {
        return ResponseEntity.ok().body(reportService.findReport(id));
    }

    @ApiOperation(value = "Mark a report as read")
    @PutMapping("/mark/{id}")
    public ResponseEntity markReport(@ApiParam(value = "Requires a report id") @PathVariable Long id) {
        return ResponseEntity.ok().body(reportService.editReport(id));
    }

    @ApiOperation(value = "Export a report")
    @GetMapping("/export/{reportId}")
    public ResponseEntity exportReport(@ApiParam(value = "Requires a report id") @PathVariable Long reportId, @ApiParam(value = "Requires a file type") @RequestParam String fileType) {
        return ResponseEntity.ok().body(reportService.exportReport(reportId, fileType));
    }
}
