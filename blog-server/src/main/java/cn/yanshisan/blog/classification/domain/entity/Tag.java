package cn.yanshisan.blog.classification.domain.entity;

import cn.yanshisan.blog.shared.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("blog_tag")
public class Tag extends BaseEntity {

    private String tagCode;
    private String tagName;
    private String treePath;
    private Integer treeDepth;
    private Integer sortOrder;
    private Integer displayStatus;
    private String description;

    public void updateMetadata(String tagName, String description, Integer displayStatus) {
        if (tagName != null && !tagName.isBlank()) {
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
        this.treeDepth = 1;
    }

    public void moveTo(Tag newParent) {
        String newPath;
        int newDepth;
        if (newParent == null) {
            newPath = "/" + this.getId() + "/";
            newDepth = 1;
        } else {
            newPath = newParent.getTreePath() + this.getId() + "/";
            newDepth = newParent.getTreeDepth() + 1;
        }
        this.treePath = newPath;
        this.treeDepth = newDepth;
    }

    public boolean isDescendantOf(Tag ancestor) {
        if (ancestor == null || ancestor.getTreePath() == null) {
            return false;
        }
        return this.treePath != null && this.treePath.startsWith(ancestor.getTreePath());
    }
}
