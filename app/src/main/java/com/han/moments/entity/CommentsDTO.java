
package com.han.moments.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Created by Han on 2016/11/21.
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