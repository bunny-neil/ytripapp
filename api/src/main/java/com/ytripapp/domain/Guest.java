package com.ytripapp.domain;

import org.hibernate.search.annotations.Indexed;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Indexed(index = "users")
@Entity
@DiscriminatorValue("Guest")
public class Guest extends User {

    private static final long serialVersionUID = 6160886345890923809L;

    public Guest() {
        group = Group.Guest;
    }

}
