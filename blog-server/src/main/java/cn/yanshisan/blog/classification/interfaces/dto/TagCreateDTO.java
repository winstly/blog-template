package cn.yanshisan.blog.classification.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TagCreateDTO {

    @NotBlank
    private String tagCode;

    @NotBlank
    private String tagName;

    private String description;

    private String parentTagCode;
}
