
package com.han.moments.entity;

import android.text.TextUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Han on 2016/11/21.
 */
public class SenderDTO {
    @JsonProperty("username")
    private String username;
    @JsonProperty("nick")
    private String nick;
    @JsonProperty("avatar")
    private String avatar;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getNick() {
        return nick;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getDisplayName() {
        return TextUtils.isEmpty(nick) ? username : nick;
    }

}