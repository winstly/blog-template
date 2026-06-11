package cn.yanshisan.blog.site.domain.service;

import cn.yanshisan.blog.shared.api.ErrorCode;
import cn.yanshisan.blog.shared.exception.BusinessException;
import cn.yanshisan.blog.site.domain.entity.SysDict;
import cn.yanshisan.blog.site.domain.repository.DictRepository;
import cn.yanshisan.blog.site.domain.vo.DictTriple;
import cn.yanshisan.blog.site.interfaces.dto.FriendLinkVO;
import cn.yanshisan.blog.site.interfaces.dto.NavItemVO;
import cn.yanshisan.blog.site.interfaces.dto.ProfileVO;
import cn.yanshisan.blog.site.interfaces.dto.SocialLinkVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DictService {

    private final DictRepository dictRepository;

    public SysDict create(SysDict dict) {
        DictTriple triple = new DictTriple(dict.getBizCode(), dict.getSubCode(), dict.getItemCode());
        if (dictRepository.existsByTriple(triple)) {
            throw new BusinessException(ErrorCode.DICT_TRIPLE_DUPLICATE);
        }
        dictRepository.save(dict);
        return dict;
    }

    public SysDict findByTriple(String bizCode, String subCode, String itemCode) {
        return dictRepository.findByTriple(new DictTriple(bizCode, subCode, itemCode))
                .orElseThrow(() -> new BusinessException(ErrorCode.DICT_NOT_FOUND));
    }

    public SysDict update(SysDict dict) {
        dictRepository.findByTriple(new DictTriple(dict.getBizCode(), dict.getSubCode(), dict.getItemCode()))
                .orElseThrow(() -> new BusinessException(ErrorCode.DICT_NOT_FOUND));
        dictRepository.update(dict);
        return dict;
    }

    public void delete(DictTriple triple) {
        dictRepository.logicalDelete(triple);
    }

    public List<FriendLinkVO> getFriendLinks() {
        List<SysDict> dicts = dictRepository.listByBizCode("friend_link");
        return dicts.stream()
                .map(d -> new FriendLinkVO(
                        d.getItemLabel(),
                        d.getItemValue(),
                        d.getSubCode(),
                        d.getRemark(),
                        d.getItemCode(),
                        d.getSortOrder()
                ))
                .toList();
    }

    public ProfileVO getProfile() {
        List<SysDict> dicts = dictRepository.listByBizAndSub("site", "profile");
        Map<String, String> values = dicts.stream()
                .collect(Collectors.toMap(SysDict::getItemCode, SysDict::getItemValue, (a, b) -> b));
        ProfileVO vo = new ProfileVO();
        vo.setNickname(values.get("nickname"));
        vo.setSignature(values.get("signature"));
        vo.setAvatar(values.get("avatar"));
        vo.setBio(values.get("bio"));
        vo.setLocation(values.get("location"));
        vo.setQq(values.get("qq"));
        vo.setEmail(values.get("email"));
        return vo;
    }

    public List<NavItemVO> getNavItems() {
        List<SysDict> dicts = dictRepository.listByBizCode("nav");
        return dicts.stream()
                .map(d -> new NavItemVO(d.getItemCode(), d.getItemLabel(), d.getItemValue()))
                .toList();
    }

    public List<SocialLinkVO> getSocialLinks() {
        List<SysDict> dicts = dictRepository.listByBizCode("social");
        return dicts.stream()
                .map(d -> new SocialLinkVO(d.getItemCode(), d.getItemLabel(), d.getItemValue()))
                .toList();
    }

    public FriendLinkVO createFriendLink(String name, String url, String logo, String description, Integer sortOrder) {
        String itemCode = "fl_" + System.currentTimeMillis();
        SysDict dict = new SysDict();
        dict.setBizCode("friend_link");
        dict.setSubCode(logo != null ? logo : "");
        dict.setItemCode(itemCode);
        dict.setItemLabel(name);
        dict.setItemValue(url);
        dict.setRemark(description);
        dict.setSortOrder(sortOrder != null ? sortOrder : 0);
        dict.setDisplayStatus(1);
        create(dict);
        return new FriendLinkVO(name, url, logo, description, itemCode, dict.getSortOrder());
    }

    public FriendLinkVO updateFriendLink(String itemCode, String name, String url, String logo, String description, Integer sortOrder) {
        SysDict dict = dictRepository.findByBizCodeAndItemCode("friend_link", itemCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.DICT_NOT_FOUND));
        if (name != null) {
            dict.setItemLabel(name);
        }
        if (url != null) {
            dict.setItemValue(url);
        }
        if (logo != null) {
            dict.setSubCode(logo);
        }
        if (description != null) {
            dict.setRemark(description);
        }
        if (sortOrder != null) {
            dict.setSortOrder(sortOrder);
        }
        dictRepository.update(dict);
        return new FriendLinkVO(dict.getItemLabel(), dict.getItemValue(), dict.getSubCode(), dict.getRemark(), dict.getItemCode(), dict.getSortOrder());
    }

    public void deleteFriendLink(String itemCode) {
        SysDict dict = dictRepository.findByBizCodeAndItemCode("friend_link", itemCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.DICT_NOT_FOUND));
        dictRepository.logicalDelete(new DictTriple(dict.getBizCode(), dict.getSubCode(), dict.getItemCode()));
    }
}
