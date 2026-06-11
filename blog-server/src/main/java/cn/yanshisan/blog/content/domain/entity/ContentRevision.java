package cn.yanshisan.blog.content.domain.entity;

import cn.yanshisan.blog.shared.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@TableName("blog_content_revision")
public class ContentRevision extends BaseEntity {

    private Long contentId;
    private String body;
    private String contentFormat;
    private Integer wordCount;
    private Integer version;
    private String changeSummary;
}
