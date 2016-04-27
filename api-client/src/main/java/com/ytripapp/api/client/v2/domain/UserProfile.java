package com.ytripapp.api.client.v2.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 1962561919971945518L;

    public enum Gender {
        Unspecified,
        Male,
        Female
    }

    String nickname;
    Gender gender = Gender.Unspecified;
    String portraitUri;
    String phoneNo;
    String firstName;
    String lastName;
    String occupation;
    String introduction;

    public static UserProfile clone(UserProfile another) {
        UserProfile copy = new UserProfile();
        copy.nickname = another.nickname;
        copy.gender = another.gender;
        copy.portraitUri = another.portraitUri;
        copy.phoneNo = another.phoneNo;
        copy.firstName = another.firstName;
        copy.lastName = another.lastName;
        copy.occupation = another.occupation;
        copy.introduction = another.introduction;
        return copy;
    }

}
