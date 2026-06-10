package cn.yanshisan.blog.site.infrastructure.ohs;

import cn.yanshisan.blog.shared.ohs.DictOHS;
import cn.yanshisan.blog.site.application.SiteApplicationService;
import cn.yanshisan.blog.site.interfaces.dto.FriendLinkVO;
import cn.yanshisan.blog.site.interfaces.dto.NavItemVO;
import cn.yanshisan.blog.site.interfaces.dto.ProfileVO;
import cn.yanshisan.blog.site.interfaces.dto.SiteConfigVO;
import cn.yanshisan.blog.site.interfaces.dto.SocialLinkVO;
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
