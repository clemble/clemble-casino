package com.clemble.casino.player;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.validator.constraints.URL;

import com.clemble.casino.error.ClembleCasinoError.Code;
import com.clemble.casino.error.validation.AgeConstraint;
import com.clemble.casino.error.validation.MaxSize;
import com.clemble.casino.error.validation.NickNameConstraint;
import com.clemble.casino.json.CustomDateFormat.CustomDateDeserializer;
import com.clemble.casino.json.CustomDateFormat.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonTypeName("native")
public class NativePlayerProfile extends PlayerProfile {

    /**
     * Generated 25/01/13
     */
    private static final long serialVersionUID = -7544343898430552989L;

    @Column(name = "NICK_NAME", length = 64)
    @JsonProperty("nickName")
    @NickNameConstraint(message = Code.NICK_INVALID_CODE)
    @MaxSize(max = 64, message = Code.NICK_TOO_LONG_CODE)
    private String nickName;

    @Column(name = "FIRST_NAME", length = 64)
    @JsonProperty("firstName")
    @MaxSize(max = 64, message = Code.FIRST_NAME_TOO_LONG_CODE)
    private String firstName;

    @Column(name = "LAST_NAME", length = 64)
    @JsonProperty("lastName")
    @MaxSize(max = 64, message = Code.LAST_NAME_TOO_LONG_CODE)
    private String lastName;

    @Column(name = "GENDER")
    @JsonProperty("gender")
    private PlayerGender gender;

    @Column(name = "BIRTH_DATE")
    @Temporal(TemporalType.DATE)
    @JsonProperty("birthDate")
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonSerialize(using = CustomDateSerializer.class)
    @AgeConstraint(message = Code.BIRTH_DATE_INVALID_CODE)
    private Date birthDate;

    @Column(name = "IMAGE_URL")
    @JsonProperty("imageUrl")
    @URL(message = Code.IMAGE_URL_INVALID_CODE)
    private String imageUrl;

    @Column(name = "CATEGORY")
    @JsonProperty("category")
    private PlayerCategory category = PlayerCategory.Novice;

    @Version
    @JsonProperty("version")
    @Column(name = "VERSION")
    private int version;

    public String getNickName() {
        return nickName;
    }

    public NativePlayerProfile setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public NativePlayerProfile setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public NativePlayerProfile setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public NativePlayerProfile setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public PlayerGender getGender() {
        return gender;
    }

    public NativePlayerProfile setGender(PlayerGender gender) {
        this.gender = gender;
        return this;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public NativePlayerProfile setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public PlayerCategory getCategory() {
        return category;
    }

    public NativePlayerProfile setCategory(PlayerCategory category) {
        this.category = category;
        return this;
    }

    @Override
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((category == null) ? 0 : category.hashCode());
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((gender == null) ? 0 : gender.hashCode());
        result = prime * result + ((imageUrl == null) ? 0 : imageUrl.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((nickName == null) ? 0 : nickName.hashCode());
        result = prime * result + (int) (version ^ (version >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NativePlayerProfile other = (NativePlayerProfile) obj;
        if (category != other.category)
            return false;
        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (gender != other.gender)
            return false;
        if (imageUrl == null) {
            if (other.imageUrl != null)
                return false;
        } else if (!imageUrl.equals(other.imageUrl))
            return false;
        if (lastName == null) {
            if (other.lastName != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;
        if (nickName == null) {
            if (other.nickName != null)
                return false;
        } else if (!nickName.equals(other.nickName))
            return false;
        return version == other.version;
    }

    @Override
    public String toString() {
        return "PlayerProfile [ nickName=" + nickName + ", firstName=" + firstName + ", lastName=" + lastName + ", gender=" + gender
                + ", birthDate=" + birthDate + ", imageUrl=" + imageUrl + ", category=" + category + ", version=" + version + "]";
    }

}
