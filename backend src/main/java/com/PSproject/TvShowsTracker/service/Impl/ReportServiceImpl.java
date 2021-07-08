package com.PSproject.TvShowsTracker.service.Impl;

import com.PSproject.TvShowsTracker.constants.FileType;
import com.PSproject.TvShowsTracker.constants.ReportType;
import com.PSproject.TvShowsTracker.dto.NewReportDto;
import com.PSproject.TvShowsTracker.dto.ReportDto;
import com.PSproject.TvShowsTracker.mapper.ReportMapper;
import com.PSproject.TvShowsTracker.model.Comment;
import com.PSproject.TvShowsTracker.model.Report;
import com.PSproject.TvShowsTracker.model.user.MyUser;
import com.PSproject.TvShowsTracker.repository.CommentRepository;
import com.PSproject.TvShowsTracker.repository.MyUserRepository;
import com.PSproject.TvShowsTracker.repository.ReportRepository;
import com.PSproject.TvShowsTracker.service.ReportService;
import com.PSproject.TvShowsTracker.utils.exporter.FileExporter;
import com.PSproject.TvShowsTracker.utils.exporter.TXTFileExporter;
import com.PSproject.TvShowsTracker.utils.exporter.XMLFileExporter;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final MyUserRepository userRepository;
    private final CommentRepository commentRepository;

    public ReportServiceImpl(ReportRepository reportRepository, MyUserRepository userRepository, CommentRepository commentRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional
    public List<ReportDto> fetchReports(boolean notViewed) {
        List<ReportDto> reports = new ArrayList<>();
        reportRepository.findAll().forEach(report -> reports.add(ReportMapper.mapModelToDto(report)));
        if (!notViewed) {
            return reports;
        }
        return reports.stream().filter(r -> r.getIsViewed().equals(Boolean.FALSE)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReportDto findReport(Long id) {
        Report report = reportRepository.findById(id).orElse(null);
        if (report == null) return null;
        return ReportMapper.mapModelToDto(report);
    }

    @Override
    @Transactional
    public List<Report> findReportByComment(Long idComment) {
        Comment comment = commentRepository.findById(idComment).orElse(null);
        if (comment == null) {
            return null;
        }
        return reportRepository.findAllByComment(comment);
    }

    @Override
    @Transactional
    public ReportDto createReport(NewReportDto newReport) {
        MyUser user = userRepository.findById(newReport.getIdUser()).orElse(null);
        Report report = Report.builder()
                .content(newReport.getContent())
                .reportType(newReport.getReportType())
                .isViewed(Boolean.FALSE)
                .sentDate(new Date())
                .user(user)
                .build();
        if (!newReport.getReportType().equals(ReportType.BUG)) {
            Comment comment = commentRepository.findById(newReport.getIdComment()).orElse(null);
            if (comment == null) return null;
            report.setComment(comment);
        }
        Report savedReport = reportRepository.save(report);
        if (user == null || savedReport == null) return null;
        return ReportMapper.mapModelToDto(savedReport);
    }

    @Override
    @Transactional
    public ReportDto editReport(Long id) {
        Report report = reportRepository.findById(id).orElse(null);
        if (report == null) return null;
        report.setIsViewed(Boolean.TRUE);
        reportRepository.save(report);

        return ReportMapper.mapModelToDto(report);
    }

    @Override
    @Transactional
    public void deleteReport(Long id) {
        Report reportToDelete = reportRepository.findById(id).orElse(null);
        if(reportToDelete != null) reportRepository.delete(reportToDelete);
    }

    @Override
    @Transactional
    public void deleteAllReport(List<Report> reports) {
        for (Report r: reports) {
            reportRepository.delete(r);
        }
    }

    @Override
    @Transactional
    public String exportReport(Long id, String fileType) {
        Report report = reportRepository.findById(id).orElse(null);
        FileExporter fileExporter;
        if (fileType.equals(FileType.TXT)) {
            fileExporter = new TXTFileExporter();
            return fileExporter.exportData(report);
        } else if (fileType.equals(FileType.XML)) {
            fileExporter = new XMLFileExporter();
            return fileExporter.exportData(report);
        }
        return null;
    }
}
