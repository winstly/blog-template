package cn.yanshisan.blog.site.interfaces.dto;

import lombok.Data;

@Data
public class ProfileVO {

    private String nickname;
    private String signature;
    private String avatar;
    private String bio;
    private String location;
    private String qq;
    private String email;
}
