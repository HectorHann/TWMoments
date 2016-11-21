package com.han.moments.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by liukun on 16/3/5.
 */
public class UserInfoDTO extends BaseDTO {

    @JsonProperty("profile-image")
    private String profile_image;
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("nick")
    private String nick;
    @JsonProperty("username")
    private String username;

}
