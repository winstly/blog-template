package com.winstly.blog.content.interfaces.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ArticleCreateRequest {

    @NotBlank(message = "标题不能为空")
    private String title;

    private String summary;

    private String coverUrl;

    @NotBlank(message = "内容不能为空")
    private String body;

    private String contentFormat;
}
