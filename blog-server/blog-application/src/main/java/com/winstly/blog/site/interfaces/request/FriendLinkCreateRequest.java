package com.winstly.blog.site.interfaces.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FriendLinkCreateRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String url;
    private String logo;
    private String description;
    private Integer sortOrder;
}
