package cn.yanshisan.blog.site.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NavItemVO {

    private String code;
    private String label;
    private String path;
}
