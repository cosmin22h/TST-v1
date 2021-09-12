package com.PSproject.TvShowsTracker.dto;

import com.PSproject.TvShowsTracker.constants.ReportType;
import com.PSproject.TvShowsTracker.dto.user.CommentDto;
import com.PSproject.TvShowsTracker.validators.ReportTypeSubset;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.constraints.Size;
import java.util.Date;

@AllArgsConstructor
@Builder
@Data
@ToString
public class ReportDto {
    private Long id;

    @ReportTypeSubset(anyOf = {ReportType.SPOILER_COMMENT, ReportType.INAPPROPRIATE_COMMENT, ReportType.BUG})
    private ReportType reportType;

    @Lob
    @Column(length=2000)
    @Size(min = 5, max = 2000, message = "The report size must be between {min} and {max} characters")
    private String content;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date sentDate;
    private Boolean isViewed;
    private String username;
    private CommentDto comment;
}
