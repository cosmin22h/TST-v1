package com.PSproject.TvShowsTracker.dto;

import com.PSproject.TvShowsTracker.constants.ReportType;
import com.PSproject.TvShowsTracker.validators.ReportTypeSubset;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Builder
@Data
public class NewReportDto {

    @ReportTypeSubset(anyOf = {ReportType.SPOILER_COMMENT, ReportType.INAPPROPRIATE_COMMENT, ReportType.BUG})
    private ReportType reportType;

    @Lob
    @Column(length=2000)
    @Size(min = 5, max = 2000, message = "The report size must be between {min} and {max} characters")
    private String content;

    @NotNull
    private Long idUser;

    private Long idComment;
}
