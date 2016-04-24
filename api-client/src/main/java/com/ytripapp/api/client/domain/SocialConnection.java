package com.ytripapp.api.client.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class SocialConnection implements Serializable {

    private static final long serialVersionUID = 5743192801325113523L;

    public enum ProviderName {
        Wechat,
        Facebook
    }

    Long id;
    ProviderName providerName;
    String connectionId;
    User user;

}
