package cn.yanshisan.blog.site.interfaces.dto;

import lombok.Data;

import java.util.List;

@Data
public class SiteConfigVO {

    private ProfileVO profile;
    private List<NavItemVO> navItems;
    private List<SocialLinkVO> socialLinks;
    private List<FriendLinkVO> friendLinks;
}
