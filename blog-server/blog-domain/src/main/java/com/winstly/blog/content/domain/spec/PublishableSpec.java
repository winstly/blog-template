package com.winstly.blog.content.domain.spec;

import com.winstly.blog.content.domain.entity.ArticleDO;
import com.winstly.blog.content.domain.entity.ContentDO;
import com.winstly.blog.content.domain.enums.PublishStatus;
import org.apache.commons.lang3.StringUtils;

public class PublishableSpec {

    public static boolean isSatisfiedBy(ArticleDO article, ContentDO content) {
        if (article == null || content == null) {
            return false;
        }
        if (StringUtils.isBlank(article.getTitle())) {
            return false;
        }
        if (StringUtils.isBlank(content.getBody())) {
            return false;
        }
        return content.getPublishStatus() != PublishStatus.PUBLISHED;
    }
}
