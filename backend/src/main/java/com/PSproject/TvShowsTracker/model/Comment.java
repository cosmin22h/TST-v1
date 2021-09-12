package com.PSproject.TvShowsTracker.model;

import com.PSproject.TvShowsTracker.model.tvshow.Episode;
import com.PSproject.TvShowsTracker.model.user.MyUser;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@XmlAccessorType(XmlAccessType.FIELD)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    @Column(length=1000)
    @Size(max = 1000, message = "Content must have maximum {max} characters")
    private String content;

    @XmlElement(name="post-date")
    @NotNull(message = "Post date is mandatory")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date postDate;

    @XmlElement(name="is-spoiler")
    private Boolean isSpoiler;

    @XmlElement(name="no-likes")
    @Min(0)
    private Long likes = 0L;

    @XmlTransient
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "episode_id")
    @JsonIgnore
    private Episode episode;

    @XmlElement(name = "user-reported")
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private MyUser myUser;

    @XmlTransient
    @ManyToMany(mappedBy = "likedComments", fetch = FetchType.EAGER)
    private List<MyUser> users;

    public String toString() {
        String commentToString = "ID - " + this.id + ", content - " + this.content + ", post date - " + this.postDate + ", is spoiler - "
                + this.isSpoiler + ", no likes - " + this.likes + ".\n";

        return commentToString;
    }
}
