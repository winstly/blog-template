package cn.yanshisan.blog.classification.interfaces.dto;

import cn.yanshisan.blog.classification.domain.entity.Tag;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TagVO {

    private Long id;
    private String tagCode;
    private String tagName;
    private String treePath;
    private Integer treeDepth;
    private Integer sortOrder;
    private Integer displayStatus;
    private String description;
    private Long articleCount;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public static TagVO from(Tag tag, Long articleCount) {
        TagVO vo = new TagVO();
        vo.setId(tag.getId());
        vo.setTagCode(tag.getTagCode());
        vo.setTagName(tag.getTagName());
        vo.setTreePath(tag.getTreePath());
        vo.setTreeDepth(tag.getTreeDepth());
        vo.setSortOrder(tag.getSortOrder());
        vo.setDisplayStatus(tag.getDisplayStatus());
        vo.setDescription(tag.getDescription());
        vo.setArticleCount(articleCount);
        vo.setGmtCreate(tag.getGmtCreate());
        vo.setGmtModified(tag.getGmtModified());
        return vo;
    }
}
