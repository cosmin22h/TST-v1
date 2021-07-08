package com.PSproject.TvShowsTracker.model;

import com.PSproject.TvShowsTracker.constants.ReportType;
import com.PSproject.TvShowsTracker.model.user.MyUser;
import com.PSproject.TvShowsTracker.validators.ReportTypeSubset;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@XmlRootElement(name = "report")
@XmlAccessorType(XmlAccessType.FIELD)
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @XmlElement(name="type")
    @ReportTypeSubset(anyOf = {ReportType.SPOILER_COMMENT, ReportType.INAPPROPRIATE_COMMENT, ReportType.BUG})
    private ReportType reportType;

    @XmlElement(name="content")
    @Lob
    @Column(length=2000)
    @Size(min = 5, max = 2000, message = "The report size must be between {min} and {max} characters")
    private String content;

    @XmlElement(name="sent-date")
    private Date sentDate;

    @XmlElement(name="is-viewed")
    @NotNull
    private Boolean isViewed;

    @XmlElement(name="user-who-reported")
    @OneToOne
    @JoinColumn(name="myuser_id", referencedColumnName = "id")
    private MyUser user;

    @XmlElement(name="comment-reported")
    @OneToOne
    @JoinColumn(name="comment_id", referencedColumnName = "id")
    private Comment comment;

    public String toString() {
        String reportToString = "Report: ID - " + this.id + ", type - " + this.reportType + ", content - " + this.content +
        ", send date - " + this.sentDate + ", is viewed - " + this.isViewed + ".\nUser who reported: " + user.toString();

        if (!this.reportType.equals(ReportType.BUG)) {
            reportToString += "Comment reported: " + comment.toString() + "User reported: " + comment.getMyUser().toString();
        }

        return reportToString;
    }
}
