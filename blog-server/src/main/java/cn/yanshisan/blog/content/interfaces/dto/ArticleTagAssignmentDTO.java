package cn.yanshisan.blog.content.interfaces.dto;

import lombok.Data;

import java.util.List;

@Data
public class ArticleTagAssignmentDTO {
    private List<String> tagCodes;
    private List<String> categoryCodes;
}
