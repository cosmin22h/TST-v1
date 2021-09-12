package com.PSproject.TvShowsTracker.model.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = BasicUser.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @NotNull
    private BasicUser user;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date expiryDate;
}
