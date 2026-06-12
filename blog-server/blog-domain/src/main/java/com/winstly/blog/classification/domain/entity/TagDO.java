package com.winstly.blog.classification.domain.entity;

import com.winstly.blog.interaction.domain.enums.DisplayStatus;
import com.winstly.blog.interaction.domain.enums.TreeConstants;
import com.winstly.blog.shared.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@TableName("blog_tag")
public class TagDO extends BaseEntity {

    private String tagCode;
    private String tagName;
    private String treePath;
    private Integer treeDepth;
    private Integer sortOrder;
    private Integer displayStatus;
    private String description;

    public void updateMetadata(String tagName, String description, Integer displayStatus) {
        if (StringUtils.isNotBlank(tagName)) {
            this.tagName = tagName;
        }
        if (description != null) {
            this.description = description;
        }
        if (displayStatus != null) {
            this.displayStatus = displayStatus;
        }
    }

    public void initTreePath(Long id) {
        this.treePath = "/" + id + "/";
        this.treeDepth = TreeConstants.ROOT_TAG_DEPTH;
    }

    public void moveTo(TagDO newParent) {
        String newPath;
        int newDepth;
        if (newParent == null) {
            newPath = "/" + this.getId() + "/";
            newDepth = TreeConstants.ROOT_TAG_DEPTH;
        } else {
            newPath = newParent.getTreePath() + this.getId() + "/";
            newDepth = newParent.getTreeDepth() + 1;
        }
        this.treePath = newPath;
        this.treeDepth = newDepth;
    }

    public boolean isDescendantOf(TagDO ancestor) {
        if (ancestor == null || ancestor.getTreePath() == null) {
            return false;
        }
        return this.treePath != null && this.treePath.startsWith(ancestor.getTreePath());
    }
}
