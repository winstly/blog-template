package cn.yanshisan.blog.site.application;

import cn.yanshisan.blog.site.domain.entity.SysDict;
import cn.yanshisan.blog.site.domain.repository.DictRepository;
import cn.yanshisan.blog.site.domain.service.DictService;
import cn.yanshisan.blog.site.domain.vo.DictTriple;
import cn.yanshisan.blog.site.interfaces.dto.FriendLinkVO;
import cn.yanshisan.blog.site.interfaces.dto.NavItemVO;
import cn.yanshisan.blog.site.interfaces.dto.ProfileVO;
import cn.yanshisan.blog.site.interfaces.dto.SiteConfigVO;
import cn.yanshisan.blog.site.interfaces.dto.SocialLinkVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SiteApplicationService {

    private final DictService dictService;
    private final DictRepository dictRepository;

    public SysDict createDict(SysDict dict) {
        return dictService.create(dict);
    }

    public SysDict updateDict(SysDict dict) {
        return dictService.update(dict);
    }

    public void deleteDict(String bizCode, String subCode, String itemCode) {
        dictService.delete(new DictTriple(bizCode, subCode, itemCode));
    }

    public List<FriendLinkVO> getFriendLinks() {
        return dictService.getFriendLinks();
    }

    public ProfileVO getProfile() {
        return dictService.getProfile();
    }

    public List<NavItemVO> getNavItems() {
        return dictService.getNavItems();
    }

    public List<SocialLinkVO> getSocialLinks() {
        return dictService.getSocialLinks();
    }

    public SiteConfigVO getSiteConfig() {
        SiteConfigVO vo = new SiteConfigVO();
        vo.setProfile(dictService.getProfile());
        vo.setNavItems(dictService.getNavItems());
        vo.setSocialLinks(dictService.getSocialLinks());
        vo.setFriendLinks(dictService.getFriendLinks());
        return vo;
    }
}
