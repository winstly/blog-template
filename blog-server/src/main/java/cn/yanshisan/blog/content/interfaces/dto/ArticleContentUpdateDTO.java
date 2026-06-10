package cn.yanshisan.blog.content.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ArticleContentUpdateDTO {

    @NotBlank(message = "内容不能为空")
    private String body;
}
