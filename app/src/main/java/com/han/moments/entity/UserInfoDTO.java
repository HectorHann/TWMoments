package com.han.moments.entity;

import android.text.TextUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Han on 2016/11/21.
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

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return TextUtils.isEmpty(nick) ? username : nick;
    }

}
