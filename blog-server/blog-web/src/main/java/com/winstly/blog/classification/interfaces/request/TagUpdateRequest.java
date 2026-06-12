package com.winstly.blog.classification.interfaces.request;

import lombok.Data;

@Data
public class TagUpdateRequest {

    private String tagName;

    private String description;

    private Integer displayStatus;
}
