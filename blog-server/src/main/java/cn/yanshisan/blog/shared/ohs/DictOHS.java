package cn.yanshisan.blog.shared.ohs;

import cn.yanshisan.blog.site.interfaces.dto.FriendLinkVO;
import cn.yanshisan.blog.site.interfaces.dto.NavItemVO;
import cn.yanshisan.blog.site.interfaces.dto.ProfileVO;
import cn.yanshisan.blog.site.interfaces.dto.SiteConfigVO;
import cn.yanshisan.blog.site.interfaces.dto.SocialLinkVO;

import java.util.List;

public interface DictOHS {

    List<FriendLinkVO> getFriendLinks();

    SiteConfigVO getSiteConfig();

    List<NavItemVO> getNavItems();

    List<SocialLinkVO> getSocialLinks();

    ProfileVO getProfile();
}
