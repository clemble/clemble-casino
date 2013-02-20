package com.gogomaya.server.player;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.validator.constraints.URL;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.validation.AgeConstraint;
import com.gogomaya.server.error.validation.NickNameConstraint;
import com.gogomaya.server.json.CustomDateFormat.CustomDateDeserializer;
import com.gogomaya.server.json.CustomDateFormat.CustomDateSerializer;

@Entity
@Table(name = "PLAYER_PROFILE")
public class PlayerProfile implements PlayerAware<PlayerProfile>, Serializable {

    /**
     * Generated 25/01/13
     */
    private static final long serialVersionUID = -7544343898430552989L;

    @Id
    @Column(name = "PLAYER_ID")
    @JsonProperty("playerId")
    private long playerId;

    @Column(name = "NICK_NAME", length = 64)
    @JsonProperty("nickName")
    @NickNameConstraint(message = GogomayaError.NICK_INVALID_CODE)
    @Size(max = 64, message = GogomayaError.NICK_TOO_LONG_CODE)
    private String nickName;

    @Column(name = "FIRST_NAME", length = 64)
    @JsonProperty("firstName")
    @Size(max = 64, message = GogomayaError.FIRST_NAME_TOO_LONG_CODE)
    private String firstName;

    @Column(name = "LAST_NAME", length = 64)
    @JsonProperty("lastName")
    @Size(max = 64, message = GogomayaError.LAST_NAME_TOO_LONG_CODE)
    private String lastName;

    @Column(name = "GENDER")
    @JsonProperty("gender")
    private Gender gender;

    @Column(name = "BIRTH_DATE")
    @Temporal(TemporalType.DATE)
    @JsonProperty("birthDate")
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonSerialize(using = CustomDateSerializer.class)
    @AgeConstraint(message = GogomayaError.BIRTH_DATE_INVALID_CODE)
    private Date birthDate;

    @Column(name = "IMAGE_URL")
    @JsonProperty("imageUrl")
    @URL(message = GogomayaError.IMAGE_URL_INVALID_CODE)
    private String imageUrl;

    @Column(name = "CATEGORY")
    @JsonProperty("category")
    private PlayerCategory category = PlayerCategory.Novice;

    @Override
    public long getPlayerId() {
        return playerId;
    }

    @Override
    public PlayerProfile setPlayerId(long userId) {
        this.playerId = userId;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public PlayerProfile setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public PlayerProfile setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public PlayerProfile setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public PlayerProfile setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public Gender getGender() {
        return gender;
    }

    public PlayerProfile setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public PlayerProfile setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public PlayerCategory getCategory() {
        return category;
    }

    public PlayerProfile setCategory(PlayerCategory category) {
        this.category = category;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((birthDate == null) ? 0 : birthDate.hashCode());
        result = prime * result + ((category == null) ? 0 : category.hashCode());
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((gender == null) ? 0 : gender.hashCode());
        result = prime * result + ((imageUrl == null) ? 0 : imageUrl.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((nickName == null) ? 0 : nickName.hashCode());
        result = prime * result + (int) (playerId ^ (playerId >>> 32));
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
        PlayerProfile other = (PlayerProfile) obj;
        if (birthDate == null) {
            if (other.birthDate != null)
                return false;
        } else if (!birthDate.equals(other.birthDate))
            return false;
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
        if (playerId != other.playerId)
            return false;
        return true;
    }

}
