package cn.yanshisan.blog.site.application;

import cn.yanshisan.blog.site.domain.entity.SysDict;
import cn.yanshisan.blog.site.domain.service.DictService;
import cn.yanshisan.blog.site.domain.vo.DictTriple;
import cn.yanshisan.blog.site.interfaces.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SiteApplicationService {

    private final DictService dictService;

    public DictResponse createDict(DictCreateRequest request) {
        SysDict dict = new SysDict();
        dict.setBizCode(request.getBizCode());
        dict.setSubCode(request.getSubCode());
        dict.setItemCode(request.getItemCode());
        dict.setItemLabel(request.getItemLabel());
        dict.setItemValue(request.getItemValue());
        dict.setExtData(request.getExtData());
        dict.setSortOrder(request.getSortOrder());
        dict.setDisplayStatus(request.getDisplayStatus());
        dict.setRemark(request.getRemark());
        SysDict saved = dictService.create(dict);
        return DictResponse.from(saved);
    }

    public DictResponse updateDict(DictUpdateRequest request) {
        SysDict dict = dictService.findByTriple(request.getBizCode(), request.getSubCode(), request.getItemCode());
        dict.setItemLabel(request.getItemLabel());
        dict.setItemValue(request.getItemValue());
        dict.setExtData(request.getExtData());
        dict.setSortOrder(request.getSortOrder());
        dict.setDisplayStatus(request.getDisplayStatus());
        dict.setRemark(request.getRemark());
        SysDict updated = dictService.update(dict);
        return DictResponse.from(updated);
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

    public FriendLinkVO createFriendLink(FriendLinkCreateDTO dto) {
        return dictService.createFriendLink(dto.getName(), dto.getUrl(), dto.getLogo(), dto.getDescription(), dto.getSortOrder());
    }

    public FriendLinkVO updateFriendLink(String itemCode, FriendLinkUpdateDTO dto) {
        return dictService.updateFriendLink(itemCode, dto.getName(), dto.getUrl(), dto.getLogo(), dto.getDescription(), dto.getSortOrder());
    }

    public void deleteFriendLink(String itemCode) {
        dictService.deleteFriendLink(itemCode);
    }
}
