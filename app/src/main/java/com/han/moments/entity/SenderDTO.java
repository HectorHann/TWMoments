/**
 * Copyright 2016 aTool.org
 */
package com.han.moments.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Auto-generated: 2016-11-20 20:52:2
 *
 * @author aTool.org (i@aTool.org)
 * @website http://www.atool.org/json2javabean.php
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

}