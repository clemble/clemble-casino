package com.gogomaya.server.user;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Email;

@Entity
@Table(name = "GAMER_PROFILE")
public class GamerProfile implements Serializable {

    /**
     * Generated 25/01/13
     */
    private static final long serialVersionUID = -7544343898430552989L;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "USER_ID")
    @JsonProperty("userId")
    private String userId;

    @Email
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "NICK_NAME")
    @JsonProperty("nickName")
    private String nickName;

    @Column(name = "FIRST_NAME")
    @JsonProperty("firstName")
    private String firstName;

    @Column(name = "LAST_NAME")
    @JsonProperty("lastName")
    private String lastName;

    @Column(name = "GENDER")
    @JsonProperty("gender")
    private Gender gender = Gender.M;

    @Column(name = "BIRTH_DATE")
    @JsonProperty("birthDate")
    @JsonDeserialize(using = CustomDateFormat.CustomDateDeserializer.class)
    @JsonSerialize(using = CustomDateFormat.CustomDateSerializer.class)
    private Date birthDate;

    @Column(name = "IMAGE_URL")
    @JsonProperty("imageUrl")
    private String imageUrl;

    @Column(name = "CATEGORY")
    @JsonProperty("category")
    private GamerCategory category = GamerCategory.Novice;

    public String getUserId() {
        return userId;
    }

    public GamerProfile setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public GamerProfile setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public GamerProfile setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public GamerProfile setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public GamerProfile setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public Gender getGender() {
        return gender;
    }

    public GamerProfile setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public GamerProfile setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public GamerProfile setEmail(String email) {
        this.email = email;
        return this;
    }

    public GamerCategory getCategory() {
        return category;
    }

    public GamerProfile setCategory(GamerCategory category) {
        this.category = category;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((birthDate == null) ? 0 : birthDate.hashCode());
        result = prime * result + ((category == null) ? 0 : category.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((gender == null) ? 0 : gender.hashCode());
        result = prime * result + ((imageUrl == null) ? 0 : imageUrl.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((nickName == null) ? 0 : nickName.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
        GamerProfile other = (GamerProfile) obj;
        if (birthDate == null) {
            if (other.birthDate != null)
                return false;
        } else if (!birthDate.equals(other.birthDate))
            return false;
        if (category != other.category)
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
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
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        return true;
    }

}
