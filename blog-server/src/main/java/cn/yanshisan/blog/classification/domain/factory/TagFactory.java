package cn.yanshisan.blog.classification.domain.factory;

import cn.yanshisan.blog.classification.domain.entity.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagFactory {

    public Tag createRootTag(String tagCode, String tagName, String description) {
        Tag tag = new Tag();
        tag.setTagCode(tagCode);
        tag.setTagName(tagName);
        tag.setSortOrder(0);
        tag.setDisplayStatus(1);
        tag.setDescription(description);
        return tag;
    }

    public Tag createChildTag(String tagCode, String tagName, String description, Tag parent) {
        Tag tag = new Tag();
        tag.setTagCode(tagCode);
        tag.setTagName(tagName);
        tag.setSortOrder(0);
        tag.setDisplayStatus(1);
        tag.setDescription(description);
        tag.setTreeDepth(parent.getTreeDepth() + 1);
        return tag;
    }
}
