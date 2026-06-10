package cn.yanshisan.blog.content.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ArticleCreateDTO {

    @NotBlank(message = "标题不能为空")
    private String title;

    private String summary;

    private String coverUrl;

    @NotBlank(message = "内容不能为空")
    private String body;

    private String contentFormat;
}
