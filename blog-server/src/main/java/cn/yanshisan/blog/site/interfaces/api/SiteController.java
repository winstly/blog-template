package cn.yanshisan.blog.site.interfaces.api;

import cn.yanshisan.blog.shared.api.R;
import cn.yanshisan.blog.site.application.SiteApplicationService;
import cn.yanshisan.blog.site.domain.entity.SysDict;
import cn.yanshisan.blog.site.interfaces.dto.FriendLinkVO;
import cn.yanshisan.blog.site.interfaces.dto.NavItemVO;
import cn.yanshisan.blog.site.interfaces.dto.ProfileVO;
import cn.yanshisan.blog.site.interfaces.dto.SiteConfigVO;
import cn.yanshisan.blog.site.interfaces.dto.SocialLinkVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SiteController {

    private final SiteApplicationService siteApplicationService;

    @GetMapping("/friend-links")
    public R<List<FriendLinkVO>> listFriendLinks() {
        return R.ok(siteApplicationService.getFriendLinks());
    }

    @GetMapping("/site-config")
    public R<SiteConfigVO> getSiteConfig() {
        return R.ok(siteApplicationService.getSiteConfig());
    }

    @GetMapping("/nav-items")
    public R<List<NavItemVO>> listNavItems() {
        return R.ok(siteApplicationService.getNavItems());
    }

    @GetMapping("/social-links")
    public R<List<SocialLinkVO>> listSocialLinks() {
        return R.ok(siteApplicationService.getSocialLinks());
    }

    @GetMapping("/profile")
    public R<ProfileVO> getProfile() {
        return R.ok(siteApplicationService.getProfile());
    }

    @PostMapping("/dicts")
    public R<SysDict> createDict(@RequestBody SysDict dict) {
        return R.ok(siteApplicationService.createDict(dict));
    }

    @PutMapping("/dicts")
    public R<SysDict> updateDict(@RequestBody SysDict dict) {
        return R.ok(siteApplicationService.updateDict(dict));
    }

    @DeleteMapping("/dicts")
    public R<Void> deleteDict(@RequestParam String bizCode,
                              @RequestParam String subCode,
                              @RequestParam String itemCode) {
        siteApplicationService.deleteDict(bizCode, subCode, itemCode);
        return R.ok();
    }
}
