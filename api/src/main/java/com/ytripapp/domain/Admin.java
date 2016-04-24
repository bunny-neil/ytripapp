package com.ytripapp.domain;

import org.hibernate.search.annotations.Indexed;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Indexed(index = "users")
@Entity
@DiscriminatorValue("Admin")
public class Admin extends User {

    private static final long serialVersionUID = 4230278488315631947L;

    public Admin() {
        group = Group.Admin;
    }

}
