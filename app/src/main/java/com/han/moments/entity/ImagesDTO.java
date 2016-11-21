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
public class ImagesDTO {

    @JsonProperty("url")
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

}