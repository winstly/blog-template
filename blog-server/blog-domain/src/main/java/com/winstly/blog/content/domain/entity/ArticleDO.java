package com.winstly.blog.content.domain.entity;

import com.winstly.blog.shared.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@TableName("blog_article")
public class ArticleDO extends BaseEntity {

    private String articleCode;
    private String title;
    private String summary;
    private String coverUrl;
    private Boolean isPinned;
    private Integer viewCount;
    private LocalDateTime publishedAt;

    public void updateMetadata(String title, String summary, String coverUrl) {
        if (StringUtils.isNotBlank(title)) {
            this.title = title;
        }
        if (summary != null) {
            this.summary = summary;
        }
        if (coverUrl != null) {
            this.coverUrl = coverUrl;
        }
    }

    public void publish() {
        this.publishedAt = LocalDateTime.now();
    }

    public void unpublish() {
        this.publishedAt = null;
    }

    public void togglePin() {
        this.isPinned = this.isPinned == null || !this.isPinned;
    }

    public void incrementViewCount() {
        this.viewCount = ObjectUtils.defaultIfNull(this.viewCount, 0) + 1;
    }
}
