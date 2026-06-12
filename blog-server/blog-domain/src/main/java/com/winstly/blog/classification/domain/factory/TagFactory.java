package com.winstly.blog.classification.domain.factory;

import com.winstly.blog.classification.domain.entity.TagDO;
import com.winstly.blog.interaction.domain.enums.DisplayStatus;
import com.winstly.blog.site.domain.enums.FriendLinkConstants;
import org.springframework.stereotype.Component;

@Component
public class TagFactory {

    public TagDO createRootTag(String tagCode, String tagName, String description) {
        TagDO tag = new TagDO();
        tag.setTagCode(tagCode);
        tag.setTagName(tagName);
        tag.setSortOrder(FriendLinkConstants.DEFAULT_SORT_ORDER);
        tag.setDisplayStatus(DisplayStatus.APPROVED.getValue());
        tag.setDescription(description);
        return tag;
    }

    public TagDO createChildTag(String tagCode, String tagName, String description, TagDO parent) {
        TagDO tag = new TagDO();
        tag.setTagCode(tagCode);
        tag.setTagName(tagName);
        tag.setSortOrder(FriendLinkConstants.DEFAULT_SORT_ORDER);
        tag.setDisplayStatus(DisplayStatus.APPROVED.getValue());
        tag.setDescription(description);
        tag.setTreeDepth(parent.getTreeDepth() + 1);
        return tag;
    }
}
