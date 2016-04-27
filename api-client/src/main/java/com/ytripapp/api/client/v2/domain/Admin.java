package com.ytripapp.api.client.v2.domain;

public class Admin extends User {

    private static final long serialVersionUID = 4230278488315631947L;

    public Admin() {
        group = Group.Admin;
    }

}
