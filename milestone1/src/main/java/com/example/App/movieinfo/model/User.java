package com.example.App.movieinfo.model;

import java.util.Objects;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "user")
public class User {
    // UserID::Gender::Age::Occupation::Zip-code
    private @MongoId Long userId;
    private String gender; // 'M' or 'F'
    private Long age;
    private Long occupation;

    public User(Long userId, String gender, Long age, Long occupation) {
        this.userId = userId;
        this.gender = gender;
        this.age = age;
        this.occupation = occupation;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setOccupation(Long occupation) {
        this.occupation = occupation;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAge() {
        return age;
    }

    public Long getOccupation() {
        return occupation;
    }

    public Long getUserId() {
        return userId;
    }

    public String getGender() {
        return gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) && Objects.equals(gender, user.gender) && Objects.equals(age, user.age) && Objects.equals(occupation, user.occupation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, gender, age, occupation);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", occupation=" + occupation +
                '}';
    }
}
