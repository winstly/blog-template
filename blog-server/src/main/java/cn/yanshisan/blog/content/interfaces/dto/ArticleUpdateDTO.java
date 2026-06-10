package cn.yanshisan.blog.content.interfaces.dto;

import lombok.Data;

@Data
public class ArticleUpdateDTO {

    private String title;
    private String summary;
    private String coverUrl;
}
