package cn.yanshisan.blog.site.interfaces.dto;

import lombok.Data;

@Data
public class FriendLinkUpdateDTO {

    private String name;
    private String url;
    private String logo;
    private String description;
    private Integer sortOrder;
}
