/**
 * Copyright 2016 aTool.org
 */
package com.han.moments.entity;

import android.text.TextUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Auto-generated: 2016-11-20 20:52:2
 *
 * @author aTool.org (i@aTool.org)
 * @website http://www.atool.org/json2javabean.php
 */
public class TweetsDTO {

    @JsonProperty("content")
    private String content;
    @JsonProperty("images")
    private List<ImagesDTO> images;
    @JsonProperty("sender")
    private SenderDTO sender;
    @JsonProperty("comments")
    private List<CommentsDTO> comments;
    @JsonProperty("error")
    private String error;
    @JsonProperty("unknown error")
    private String unknown_error;

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setImages(List<ImagesDTO> images) {
        this.images = images;
    }

    public List<ImagesDTO> getImages() {
        return images;
    }

    public void setSender(SenderDTO sender) {
        this.sender = sender;
    }

    public SenderDTO getSender() {
        return sender;
    }

    public void setComments(List<CommentsDTO> comments) {
        this.comments = comments;
    }

    public List<CommentsDTO> getComments() {
        return comments;
    }

    public boolean isUsefulTweet() {
        return TextUtils.isEmpty(error) && TextUtils.isEmpty(unknown_error);
    }

}