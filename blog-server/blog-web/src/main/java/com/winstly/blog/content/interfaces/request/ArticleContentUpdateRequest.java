package com.winstly.blog.content.interfaces.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ArticleContentUpdateRequest {

    @NotBlank(message = "内容不能为空")
    private String body;
}
