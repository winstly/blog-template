package com.winstly.blog.content.domain.entity;

import com.winstly.blog.shared.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@TableName("blog_content_revision")
public class ContentRevisionDO extends BaseEntity {

    private Long contentId;
    private String body;
    private String contentFormat;
    private Integer wordCount;
    private Integer version;
    private String changeSummary;
}
