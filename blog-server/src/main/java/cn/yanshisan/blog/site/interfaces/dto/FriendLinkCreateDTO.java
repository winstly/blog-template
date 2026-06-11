package cn.yanshisan.blog.site.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FriendLinkCreateDTO {

    @NotBlank
    private String name;
    @NotBlank
    private String url;
    private String logo;
    private String description;
    private Integer sortOrder;
}
