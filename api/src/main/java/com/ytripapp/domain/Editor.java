package com.ytripapp.domain;

import org.hibernate.search.annotations.Indexed;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Indexed(index = "users")
@Entity
@DiscriminatorValue("Editor")
public class Editor extends User {

    private static final long serialVersionUID = 4552242554355989415L;

    public Editor() {
        group = Group.Editor;
    }

}
