package com.PSproject.TvShowsTracker.mapper;

import com.PSproject.TvShowsTracker.dto.ReportDto;
import com.PSproject.TvShowsTracker.model.Report;

public class ReportMapper {
    public static ReportDto mapModelToDto(Report report) {
        ReportDto reportDto = ReportDto.builder()
                .id(report.getId())
                .content(report.getContent())
                .isViewed(report.getIsViewed())
                .reportType(report.getReportType())
                .username(report.getUser().getUsername())
                .sentDate(report.getSentDate())
                .build();

        if (report.getComment() != null) {
            reportDto.setComment(CommentMapper.mapModelToDto(report.getComment()));
        }

        return reportDto;
    }
}
