package com.ytripapp.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.SortableField;
import org.hibernate.search.annotations.Store;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 1962561919971945518L;

    public enum Gender {
        Unspecified,
        Male,
        Female
    }

    @Field(store = Store.YES, analyze = Analyze.NO)
    @SortableField
    String nickname;

    @Field(store = Store.YES, analyze = Analyze.NO)
    @Enumerated(EnumType.STRING)
    Gender gender = Gender.Unspecified;

    @Field(store = Store.YES, analyze = Analyze.NO, index = Index.NO)
    String portraitUri;

    @Field(store = Store.YES, analyze = Analyze.NO)
    @SortableField
    String phoneNo;

    @Field(store = Store.YES, analyze = Analyze.NO)
    @SortableField
    String firstName;

    @Field(store = Store.YES, analyze = Analyze.NO)
    @SortableField
    String lastName;

    @Field(store = Store.YES, analyze = Analyze.NO)
    String occupation;

    @Field(store = Store.YES, analyze = Analyze.NO)
    @Column(columnDefinition = "LONGTEXT")
    String introduction;

}
