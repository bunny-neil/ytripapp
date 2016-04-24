package com.ytripapp.domain;

import org.hibernate.search.annotations.Indexed;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Indexed(index = "users")
@Entity
@DiscriminatorValue("Host")
public class Host extends User {

    private static final long serialVersionUID = -7733900132150123817L;

    public Host() {
        group = Group.Host;
    }

}
