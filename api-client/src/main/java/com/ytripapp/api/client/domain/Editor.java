package com.ytripapp.api.client.domain;

public class Editor extends User {

    private static final long serialVersionUID = 4552242554355989415L;

    public Editor() {
        group = Group.Editor;
    }

}
