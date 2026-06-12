package com.winstly.blog.content.interfaces.request;

import lombok.Data;

import java.util.List;

@Data
public class ArticleTagAssignmentRequest {
    private List<String> tagCodes;
    private List<String> categoryCodes;
}
