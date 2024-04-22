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

    // returns true iff the gender string is 'valid'.
    public static boolean genderChecker(String g) {
        return g.equals("F") || g.equals("M");
    }

    /*
     *  1:  "Under 18"
     * 18:  "18-24"
     * 25:  "25-34"
     * 35:  "35-44"
     * 45:  "45-49"
     * 50:  "50-55"
     * 56:  "56+"
     */
    public static long ageMapper(Long age) {
        if(age<18L) return 1L;
        if(age<25L) return 18L;
        if(age<35L) return 25L;
        if(age<45L) return 35L;
        if(age<50L) return 45L;
        if(age<56L) return 50L;
        return 56L;
    }

    public static boolean occChecker(Long occ) {
        return occ>=0L && occ<=20L;
    }

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
