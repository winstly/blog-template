package com.winstly.blog.site.infrastructure.ohs;

import com.winstly.blog.shared.ohs.DictOHS;
import com.winstly.blog.site.application.SiteApplicationService;
import com.winstly.blog.site.interfaces.vo.FriendLinkVO;
import com.winstly.blog.site.interfaces.vo.NavItemVO;
import com.winstly.blog.site.interfaces.vo.ProfileVO;
import com.winstly.blog.site.interfaces.vo.SiteConfigVO;
import com.winstly.blog.site.interfaces.vo.SocialLinkVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DictOHSImpl implements DictOHS {

    private final SiteApplicationService siteApplicationService;

    @Override
    public List<FriendLinkVO> getFriendLinks() {
        return siteApplicationService.getFriendLinks();
    }

    @Override
    public SiteConfigVO getSiteConfig() {
        return siteApplicationService.getSiteConfig();
    }

    @Override
    public List<NavItemVO> getNavItems() {
        return siteApplicationService.getNavItems();
    }

    @Override
    public List<SocialLinkVO> getSocialLinks() {
        return siteApplicationService.getSocialLinks();
    }

    @Override
    public ProfileVO getProfile() {
        return siteApplicationService.getProfile();
    }
}
