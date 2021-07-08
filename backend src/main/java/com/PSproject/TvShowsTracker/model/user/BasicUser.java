package com.PSproject.TvShowsTracker.model.user;

import com.PSproject.TvShowsTracker.constants.Role;
import com.PSproject.TvShowsTracker.validators.UserRoleSubset;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import javax.xml.bind.annotation.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@XmlAccessorType(XmlAccessType.FIELD)
public class BasicUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Username is mandatory")
    @Size(min = 5, message = "Username must have at least {min} characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Username can't contains special characters")
    private String username;

    @NotNull(message = "Email is mandatory")
    @Email
    private String email;

    @XmlElement(name = "is-active")
    @NotNull(message = "Active status is mandatory")
    private Boolean isActive;

    @XmlTransient
    @UserRoleSubset(anyOf = {Role.USER, Role.ADMIN})
    private Role role;

    @XmlElement(name = "date-joined")
    @NotNull(message = "Date joined is mandatory")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date dateJoined;

    @XmlTransient
    @NotNull(message = "Password is mandatory")
    @Size(min = 8, message = "Password must have at least {min} characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*\\W).*$",
            message = "Password must contains  at least 1 uppercase, 1 lowercase, 1 digit, 1 special character")
    private String password;

    @XmlElement(name = "auth-session")
    @OneToOne
    @JoinColumn(name="authSession_id", referencedColumnName = "id")
    private AuthSession authSession;

    public String toString() {
        String userToString = "ID - " + this.id + ", username - " + this.username + ", email - " + this.email +  ", is active - " + this.isActive +
        ", date joined - " + this.dateJoined + ", last login - " + this.authSession.getDateLastLogin() + ", last logout - " + this.authSession.getDateLastLogout() + ".\n";

        return userToString;
    }

}
