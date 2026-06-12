package com.winstly.blog.site.interfaces.request;

import lombok.Data;

@Data
public class FriendLinkUpdateRequest {

    private String name;
    private String url;
    private String logo;
    private String description;
    private Integer sortOrder;
}
