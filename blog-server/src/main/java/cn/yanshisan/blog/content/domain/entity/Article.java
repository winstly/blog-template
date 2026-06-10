package cn.yanshisan.blog.content.domain.entity;

import cn.yanshisan.blog.shared.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("blog_article")
public class Article extends BaseEntity {

    private String articleCode;
    private String title;
    private String summary;
    private String coverUrl;
    private Boolean isPinned;
    private Integer viewCount;
    private LocalDateTime publishedAt;

    public void updateMetadata(String title, String summary, String coverUrl) {
        if (title != null && !title.isBlank()) {
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

    public void togglePin() {
        this.isPinned = this.isPinned == null || !this.isPinned;
    }

    public void incrementViewCount() {
        this.viewCount = this.viewCount + 1;
    }
}
