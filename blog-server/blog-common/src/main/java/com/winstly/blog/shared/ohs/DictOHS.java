package com.winstly.blog.shared.ohs;

import com.winstly.blog.site.interfaces.vo.FriendLinkVO;
import com.winstly.blog.site.interfaces.vo.NavItemVO;
import com.winstly.blog.site.interfaces.vo.ProfileVO;
import com.winstly.blog.site.interfaces.vo.SiteConfigVO;
import com.winstly.blog.site.interfaces.vo.SocialLinkVO;

import java.util.List;

public interface DictOHS {

    List<FriendLinkVO> getFriendLinks();

    SiteConfigVO getSiteConfig();

    List<NavItemVO> getNavItems();

    List<SocialLinkVO> getSocialLinks();

    ProfileVO getProfile();
}
