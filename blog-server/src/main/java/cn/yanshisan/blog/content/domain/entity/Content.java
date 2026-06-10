package cn.yanshisan.blog.content.domain.entity;

import cn.yanshisan.blog.content.domain.vo.PublishStatus;
import cn.yanshisan.blog.shared.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("blog_content")
public class Content extends BaseEntity {

    private String targetType;
    private String targetCode;
    private String body;
    private String contentFormat;
    private Integer wordCount;
    private Integer version;
    private PublishStatus publishStatus;

    public static Content createForArticle(String articleCode, String body, String contentFormat) {
        Content content = new Content();
        content.targetType = "article";
        content.targetCode = articleCode;
        content.body = body;
        content.contentFormat = contentFormat != null ? contentFormat : "markdown";
        content.wordCount = countWords(body);
        content.version = 1;
        content.publishStatus = PublishStatus.DRAFT;
        return content;
    }

    public void updateBody(String body) {
        this.body = body;
        this.wordCount = countWords(body);
        this.version = this.version + 1;
    }

    public void publish() {
        this.publishStatus = PublishStatus.PUBLISHED;
    }

    public ContentRevision createRevision(String changeSummary) {
        ContentRevision revision = new ContentRevision();
        revision.setContentId(this.getId());
        revision.setBody(this.body);
        revision.setContentFormat(this.contentFormat);
        revision.setWordCount(this.wordCount);
        revision.setVersion(this.version);
        revision.setChangeSummary(changeSummary);
        return revision;
    }

    private static int countWords(String text) {
        if (text == null || text.isBlank()) {
            return 0;
        }
        return text.split("\\s+").length;
    }
}
