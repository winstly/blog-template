package com.winstly.blog.interaction.interfaces.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MessageCreateRequest {

    @NotBlank
    private String userName;

    private String userAvatarUrl;

    @NotBlank
    private String remark;

    private Long parentId;

    private String extMeta;
}
