package com.gogomaya.server.security;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.cloudfoundry.org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Email;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.validation.PasswordConstraint;

@Entity
@Table(name = "USER_CREDENTIALS")
public class GamerCredential implements Serializable {

    /**
     * Generated 15/02/13
     */
    private static final long serialVersionUID = 6796999437224779009L;

    @Id
    @Column(name = "PROFILE_ID")
    @JsonProperty("profileId")
    private int profileId;

    @Column(name = "EMAIL")
    @JsonProperty("email")
    @Email(message = GogomayaError.EMAIL_INVALID_CODE)
    private String email;

    @Column(name = "CREDENTIAL_TYPE")
    @JsonIgnore
    private CredentialType credentialType;

    @Column(name = "PASSWORD")
    @JsonProperty("password")
    @PasswordConstraint
    @Min(value = 6, message = GogomayaError.PASSWORD_TOO_SHORT_CODE)
    @Max(value = 32, message = GogomayaError.PASSWORD_TOO_LONG_CODE)
    @NotNull(message = GogomayaError.PASSWORD_MISSING_CODE)
    private String password;

    public String getEmail() {
        return email;
    }

    public GamerCredential setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public GamerCredential setPassword(String password) {
        this.password = password;
        return this;
    }

    public int getProfileId() {
        return profileId;
    }

    public GamerCredential setProfileId(int profileId) {
        this.profileId = profileId;
        return this;
    }

    public CredentialType getCredentialType() {
        return credentialType;
    }

    public GamerCredential setCredentialType(CredentialType credentialType) {
        this.credentialType = credentialType;
        return this;
    }

}
