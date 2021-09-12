package com.PSproject.TvShowsTracker.model.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class AuthSession {
    @XmlTransient
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @XmlTransient
    private Boolean isActive;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "Europe/Bucharest")
    private Date dateLastLogin;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "Europe/Bucharest")
    private Date dateLastLogout;

}
