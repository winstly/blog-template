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

    public SysDict update(SysDict dict) {
        dictRepository.findByTriple(new DictTriple(dict.getBizCode(), dict.getSubCode(), dict.getItemCode()))
                .orElseThrow(() -> new BusinessException(ErrorCode.DICT_TRIPLE_DUPLICATE));
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
                        d.getRemark()
                ))
                .toList();
    }

    public ProfileVO getProfile() {
        List<SysDict> dicts = dictRepository.listByBizAndSub("site", "profile");
        ProfileVO vo = new ProfileVO();
        for (SysDict d : dicts) {
            switch (d.getItemCode()) {
                case "nickname" -> vo.setNickname(d.getItemValue());
                case "signature" -> vo.setSignature(d.getItemValue());
                case "avatar" -> vo.setAvatar(d.getItemValue());
                case "bio" -> vo.setBio(d.getItemValue());
                case "location" -> vo.setLocation(d.getItemValue());
                case "qq" -> vo.setQq(d.getItemValue());
                case "email" -> vo.setEmail(d.getItemValue());
            }
        }
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
}
