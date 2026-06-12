package com.winstly.blog.content.interfaces.request;

import lombok.Data;

@Data
public class ArticleUpdateRequest {

    private String title;
    private String summary;
    private String coverUrl;
}
