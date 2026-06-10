package cn.yanshisan.blog.interaction.domain.entity;

import cn.yanshisan.blog.interaction.domain.vo.Action;
import cn.yanshisan.blog.shared.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("blog_interaction")
public class Interaction extends BaseEntity {

    private String userName;
    private String userAvatarUrl;
    private String targetType;
    private String targetCode;
    private Action actionType;
    private String remark;
    private String treePath;
    private Integer treeDepth;
    private String extMeta;

    public boolean isRoot() {
        return treeDepth != null && treeDepth == 0;
    }

    public void fillTreePath(String path) {
        this.treePath = path;
    }
}
