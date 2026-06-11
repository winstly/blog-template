package cn.yanshisan.blog.site.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendLinkVO {

    private String name;
    private String url;
    private String logo;
    private String description;
    private String itemCode;
    private Integer sortOrder;

    public FriendLinkVO(String name, String url, String logo, String description) {
        this.name = name;
        this.url = url;
        this.logo = logo;
        this.description = description;
    }
}
