package cn.yanshisan.blog.site.interfaces.api;

import cn.yanshisan.blog.shared.api.R;
import cn.yanshisan.blog.site.application.SiteApplicationService;
import cn.yanshisan.blog.site.interfaces.dto.*;
import jakarta.validation.Valid;
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
    public R<DictResponse> createDict(@RequestBody @Valid DictCreateRequest request) {
        return R.ok(siteApplicationService.createDict(request));
    }

    @PutMapping("/dicts")
    public R<DictResponse> updateDict(@RequestBody @Valid DictUpdateRequest request) {
        return R.ok(siteApplicationService.updateDict(request));
    }

    @DeleteMapping("/dicts")
    public R<Void> deleteDict(@RequestParam String bizCode,
                              @RequestParam String subCode,
                              @RequestParam String itemCode) {
        siteApplicationService.deleteDict(bizCode, subCode, itemCode);
        return R.ok();
    }

    @PostMapping("/friend-links")
    public R<FriendLinkVO> createFriendLink(@RequestBody @Valid FriendLinkCreateDTO dto) {
        return R.ok(siteApplicationService.createFriendLink(dto));
    }

    @PutMapping("/friend-links/{itemCode}")
    public R<FriendLinkVO> updateFriendLink(@PathVariable String itemCode,
                                            @RequestBody FriendLinkUpdateDTO dto) {
        return R.ok(siteApplicationService.updateFriendLink(itemCode, dto));
    }

    @DeleteMapping("/friend-links/{itemCode}")
    public R<Void> deleteFriendLink(@PathVariable String itemCode) {
        siteApplicationService.deleteFriendLink(itemCode);
        return R.ok();
    }
}
