package com.winstly.blog.content.domain.entity;

import com.winstly.blog.content.domain.enums.ContentConstants;
import com.winstly.blog.content.domain.enums.ContentFormat;
import com.winstly.blog.content.domain.enums.PublishStatus;
import com.winstly.blog.shared.constants.TargetType;
import com.winstly.blog.shared.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@TableName("blog_content")
public class ContentDO extends BaseEntity {

    private String targetType;
    private String targetCode;
    private String body;
    private String contentFormat;
    private Integer wordCount;
    private Integer version;
    private PublishStatus publishStatus;

    public static ContentDO createForArticle(String articleCode, String body, String contentFormat) {
        ContentDO content = new ContentDO();
        content.targetType = TargetType.ARTICLE;
        content.targetCode = articleCode;
        content.body = body;
        content.contentFormat = ObjectUtils.defaultIfNull(contentFormat, ContentFormat.MARKDOWN.getValue());
        content.wordCount = countWords(body);
        content.version = ContentConstants.INITIAL_VERSION;
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

    public void unpublish() {
        this.publishStatus = PublishStatus.DRAFT;
    }

    public ContentRevisionDO createRevision(String changeSummary) {
        ContentRevisionDO revision = new ContentRevisionDO();
        revision.setContentId(this.getId());
        revision.setBody(this.body);
        revision.setContentFormat(this.contentFormat);
        revision.setWordCount(this.wordCount);
        revision.setVersion(this.version);
        revision.setChangeSummary(changeSummary);
        return revision;
    }

    private static int countWords(String text) {
        if (StringUtils.isBlank(text)) {
            return 0;
        }
        return text.split("\\s+").length;
    }
}
