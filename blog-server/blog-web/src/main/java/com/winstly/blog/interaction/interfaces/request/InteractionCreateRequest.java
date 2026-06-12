package com.winstly.blog.interaction.interfaces.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InteractionCreateRequest {

    @NotBlank
    private String targetType;

    @NotBlank
    private String targetCode;

    @NotBlank
    private String actionType;

    @NotBlank
    private String userName;

    private String userAvatarUrl;

    @NotBlank
    private String remark;

    private Long parentId;

    private String extMeta;
}
