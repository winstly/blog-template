package com.winstly.blog.site.domain.service;

import com.winstly.blog.interaction.domain.enums.DisplayStatus;
import com.winstly.blog.shared.api.ErrorCode;
import com.winstly.blog.shared.exception.BusinessException;
import com.winstly.blog.site.domain.enums.DictBizCode;
import com.winstly.blog.site.domain.enums.DictSubCode;
import com.winstly.blog.site.domain.enums.FriendLinkConstants;
import com.winstly.blog.site.domain.enums.ProfileItemCode;
import com.winstly.blog.site.domain.entity.SysDictDO;
import com.winstly.blog.site.domain.repository.DictRepository;
import com.winstly.blog.site.domain.valueobject.DictTriple;
import com.winstly.blog.site.interfaces.vo.FriendLinkVO;
import com.winstly.blog.site.interfaces.vo.NavItemVO;
import com.winstly.blog.site.interfaces.vo.ProfileVO;
import com.winstly.blog.site.interfaces.vo.SocialLinkVO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DictService {

    private final DictRepository dictRepository;

    public SysDictDO create(SysDictDO dict) {
        DictTriple triple = new DictTriple(dict.getBizCode(), dict.getSubCode(), dict.getItemCode());
        if (dictRepository.existsByTriple(triple)) {
            throw new BusinessException(ErrorCode.DICT_TRIPLE_DUPLICATE);
        }
        dictRepository.save(dict);
        return dict;
    }

    public SysDictDO findByTriple(String bizCode, String subCode, String itemCode) {
        return dictRepository.findByTriple(new DictTriple(bizCode, subCode, itemCode))
                .orElseThrow(() -> new BusinessException(ErrorCode.DICT_NOT_FOUND));
    }

    public SysDictDO update(SysDictDO dict) {
        dictRepository.findByTriple(new DictTriple(dict.getBizCode(), dict.getSubCode(), dict.getItemCode()))
                .orElseThrow(() -> new BusinessException(ErrorCode.DICT_NOT_FOUND));
        dictRepository.update(dict);
        return dict;
    }

    public void delete(DictTriple triple) {
        dictRepository.logicalDelete(triple);
    }

    public List<FriendLinkVO> getFriendLinks() {
        List<SysDictDO> dicts = dictRepository.listByBizCode(DictBizCode.FRIEND_LINK);
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
        List<SysDictDO> dicts = dictRepository.listByBizAndSub(DictBizCode.SITE, DictSubCode.PROFILE);
        Map<String, String> values = dicts.stream()
                .collect(Collectors.toMap(SysDictDO::getItemCode, SysDictDO::getItemValue, (a, b) -> b));
        ProfileVO vo = new ProfileVO();
        vo.setNickname(values.get(ProfileItemCode.NICKNAME));
        vo.setSignature(values.get(ProfileItemCode.SIGNATURE));
        vo.setAvatar(values.get(ProfileItemCode.AVATAR));
        vo.setBio(values.get(ProfileItemCode.BIO));
        vo.setLocation(values.get(ProfileItemCode.LOCATION));
        vo.setQq(values.get(ProfileItemCode.QQ));
        vo.setEmail(values.get(ProfileItemCode.EMAIL));
        return vo;
    }

    public List<NavItemVO> getNavItems() {
        List<SysDictDO> dicts = dictRepository.listByBizCode(DictBizCode.NAV);
        return dicts.stream()
                .map(d -> new NavItemVO(d.getItemCode(), d.getItemLabel(), d.getItemValue()))
                .toList();
    }

    public List<SocialLinkVO> getSocialLinks() {
        List<SysDictDO> dicts = dictRepository.listByBizCode(DictBizCode.SOCIAL);
        return dicts.stream()
                .map(d -> new SocialLinkVO(d.getItemCode(), d.getItemLabel(), d.getItemValue()))
                .toList();
    }

    public FriendLinkVO createFriendLink(String name, String url, String logo, String description, Integer sortOrder) {
        String itemCode = FriendLinkConstants.CODE_PREFIX + System.currentTimeMillis();
        SysDictDO dict = new SysDictDO();
        dict.setBizCode(DictBizCode.FRIEND_LINK);
        dict.setSubCode(StringUtils.defaultString(logo));
        dict.setItemCode(itemCode);
        dict.setItemLabel(name);
        dict.setItemValue(url);
        dict.setRemark(description);
        dict.setSortOrder(ObjectUtils.defaultIfNull(sortOrder, FriendLinkConstants.DEFAULT_SORT_ORDER));
        dict.setDisplayStatus(DisplayStatus.APPROVED.getValue());
        create(dict);
        return new FriendLinkVO(name, url, logo, description, itemCode, dict.getSortOrder());
    }

    public FriendLinkVO updateFriendLink(String itemCode, String name, String url, String logo, String description, Integer sortOrder) {
        SysDictDO dict = dictRepository.findByBizCodeAndItemCode(DictBizCode.FRIEND_LINK, itemCode)
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
        SysDictDO dict = dictRepository.findByBizCodeAndItemCode(DictBizCode.FRIEND_LINK, itemCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.DICT_NOT_FOUND));
        dictRepository.logicalDelete(new DictTriple(dict.getBizCode(), dict.getSubCode(), dict.getItemCode()));
    }
}
