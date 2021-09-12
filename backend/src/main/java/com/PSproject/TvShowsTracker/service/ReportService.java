package com.PSproject.TvShowsTracker.service;

import com.PSproject.TvShowsTracker.dto.NewReportDto;
import com.PSproject.TvShowsTracker.dto.ReportDto;
import com.PSproject.TvShowsTracker.model.Report;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ReportService {
    List<ReportDto> fetchReports(boolean notViewed);
    ReportDto findReport(Long id);
    List<Report> findReportByComment(Long idComment);
    ReportDto createReport(NewReportDto newReport);
    ReportDto editReport(Long id);
    void deleteReport(Long id);
    void deleteAllReport(List<Report> reports);
    String exportReport(Long id, String fileType);
}
