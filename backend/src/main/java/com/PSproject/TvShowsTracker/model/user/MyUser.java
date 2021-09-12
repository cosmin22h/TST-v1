package com.PSproject.TvShowsTracker.model.user;

import com.PSproject.TvShowsTracker.constants.Role;
import com.PSproject.TvShowsTracker.model.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import java.util.*;

@Entity
@NoArgsConstructor
@Data
@XmlTransient
public class MyUser extends BasicUser {

    @Lob
    @Column(name="avatar")
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] avatar;

    @NotBlank(message = "Display name is mandatory")
    @Size(min = 3, message = "Display name must have at least {min} characters")
    private String displayName;

    @Lob
    @Column(length=1000)
    @Size(max = 1000, message = "About must have maximum {max} characters")
    private String about;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date birthday;

    private String gender;
    private String country;
    private String facebook;
    private String instagram;
    private String twitter;
    private String reddit;

    @OneToMany(targetEntity = ListTvShows.class, mappedBy = "myUser", cascade = CascadeType.ALL)
    List<ListTvShows> lists = new ArrayList<>();

    @OneToMany(targetEntity = UserEpisode.class, mappedBy = "myUser", cascade = CascadeType.ALL)
    List<UserEpisode> episodesViewed = new ArrayList<>();

    @OneToMany(targetEntity = Friends.class, mappedBy = "myUser", cascade = CascadeType.ALL)
    List<Friends> following = new ArrayList<>();

    @OneToMany(targetEntity = Friends.class, mappedBy = "friend", cascade = CascadeType.ALL)
    List<Friends> followers = new ArrayList<>();

    @OneToMany(targetEntity = Comment.class, mappedBy = "myUser", cascade = CascadeType.ALL)
    List<Comment> comments = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "liked_comment",
            joinColumns = @JoinColumn(name = "myuser_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id",
                    referencedColumnName = "id"))
    private List<Comment> likedComments;

    public MyUser(Long id, String username, String email, Boolean isActive, Date dateJoined, String password, AuthSession authSession, String displayName, String about, Date birthday, String gender, String country, String facebook, String instagram, String twitter, String reddit, Set<MyUser> followers, Set<MyUser> following, List<Comment> likedComments) {
        super(id, username, email, isActive, Role.USER, dateJoined, password, authSession);
        this.displayName = displayName;
        this.about = about;
        this.birthday = birthday;
        this.gender = gender;
        this.country = country;
        this.facebook = facebook;
        this.instagram = instagram;
        this.twitter = twitter;
        this.reddit = reddit;
        this.likedComments = likedComments;
    }

    public String toString() {
        return super.toString();
    }
}
