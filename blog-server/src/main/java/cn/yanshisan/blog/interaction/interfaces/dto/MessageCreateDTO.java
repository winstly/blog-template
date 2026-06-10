package cn.yanshisan.blog.interaction.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MessageCreateDTO {

    @NotBlank
    private String userName;

    private String userAvatarUrl;

    @NotBlank
    private String remark;

    private Long parentId;

    private String extMeta;
}
