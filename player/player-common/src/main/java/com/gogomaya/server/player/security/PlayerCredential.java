package com.gogomaya.server.player.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.cloudfoundry.org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.Email;

import com.gogomaya.server.error.GogomayaError.Code;
import com.gogomaya.server.error.validation.MaxSize;
import com.gogomaya.server.error.validation.MinSize;
import com.gogomaya.server.error.validation.PasswordConstraint;
import com.gogomaya.server.player.PlayerAware;

@Entity
@Table(name = "PLAYER_CREDENTIALS")
public class PlayerCredential implements PlayerAware {

    /**
     * Generated 15/02/13
     */
    private static final long serialVersionUID = 6796999437224779009L;

    @Id
    @Column(name = "PLAYER_ID")
    @JsonProperty("playerId")
    private long playerId;

    @Column(name = "EMAIL", unique = true, length = 128)
    @JsonProperty("email")
    @Email(message = Code.EMAIL_INVALID_CODE)
    private String email;

    @Column(name = "PASSWORD")
    @JsonProperty("password")
    @PasswordConstraint
    @MinSize(min = 6, message = Code.PASSWORD_TOO_SHORT_CODE)
    @MaxSize(max = 64, message = Code.PASSWORD_TOO_LONG_CODE)
    @NotNull(message = Code.PASSWORD_MISSING_CODE)
    private String password;

    @Override
    public long getPlayerId() {
        return playerId;
    }

    public PlayerCredential setPlayerId(long profileId) {
        this.playerId = profileId;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public PlayerCredential setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public PlayerCredential setPassword(String password) {
        this.password = password;
        return this;
    }

}