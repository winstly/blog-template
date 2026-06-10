package cn.yanshisan.blog.classification.interfaces.dto;

import lombok.Data;

@Data
public class TagUpdateDTO {

    private String tagName;

    private String description;

    private Integer displayStatus;
}
