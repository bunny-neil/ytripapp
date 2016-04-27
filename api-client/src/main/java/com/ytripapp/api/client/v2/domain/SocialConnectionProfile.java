package com.ytripapp.api.client.v2.domain;

import java.util.Optional;

public interface SocialConnectionProfile {

    Optional<String> provideEmail();

    SocialConnection socialConnection();

    UserProfile userProfile();

}
