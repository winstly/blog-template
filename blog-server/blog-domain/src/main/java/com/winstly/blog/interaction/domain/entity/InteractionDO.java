package com.winstly.blog.interaction.domain.entity;

import com.winstly.blog.interaction.domain.enums.Action;
import com.winstly.blog.interaction.domain.enums.TreeConstants;
import com.winstly.blog.shared.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@TableName("blog_interaction")
public class InteractionDO extends BaseEntity {

    private String userName;
    private String userAvatarUrl;
    private String targetType;
    private String targetCode;
    private Action actionType;
    private String remark;
    private String treePath;
    private Integer treeDepth;
    private String extMeta;
    private Integer displayStatus;  // 0=pending, 1=approved

    public boolean isRoot() {
        return treeDepth != null && treeDepth == TreeConstants.ROOT_DEPTH;
    }

    public void fillTreePath(String path) {
        this.treePath = path;
    }
}
