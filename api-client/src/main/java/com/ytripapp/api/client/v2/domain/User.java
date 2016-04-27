package com.ytripapp.api.client.v2.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@JsonTypeInfo(property = "group", use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes({
    @JsonSubTypes.Type(name = "Guest", value = Guest.class),
    @JsonSubTypes.Type(name = "Host", value = Host.class),
    @JsonSubTypes.Type(name = "Admin", value = Admin.class),
    @JsonSubTypes.Type(name = "Editor", value = Editor.class)
})
public abstract class User implements Serializable {

    private static final long serialVersionUID = -7820800856438140841L;

    Long id;
    Long version;
    boolean enabled = true;
    Date dateCreated = new Date();
    Date dateUpdated;
    Date dateLastSignIn;
    Group group;
    String emailAddress;
    String password;
    String stripeCustId;
    String apnsDeviceToken;
    UserProfile profile;
    List<SocialConnection> connections = new ArrayList<>();
    Set<Authority> authorities = new HashSet<>();

}
