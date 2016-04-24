package com.ytripapp.domain;

import org.hibernate.search.annotations.Indexed;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Indexed(index = "users")
@Entity
@DiscriminatorValue("Gateway")
public class Gateway extends User {

    private static final long serialVersionUID = 3605943340478991351L;

    public Gateway() {
        group = Group.Gateway;
    }

}
