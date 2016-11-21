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
public class CommentsDTO {

    @JsonProperty("content")
    private String content;
    @JsonProperty("sender")
    private SenderDTO sender;

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setSender(SenderDTO sender) {
        this.sender = sender;
    }

    public SenderDTO getSender() {
        return sender;
    }

}