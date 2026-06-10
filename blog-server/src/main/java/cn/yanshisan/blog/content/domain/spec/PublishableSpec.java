package cn.yanshisan.blog.content.domain.spec;

import cn.yanshisan.blog.content.domain.entity.Article;
import cn.yanshisan.blog.content.domain.entity.Content;
import cn.yanshisan.blog.content.domain.vo.PublishStatus;

public class PublishableSpec {

    public static boolean isSatisfiedBy(Article article, Content content) {
        if (article == null || content == null) {
            return false;
        }
        if (article.getTitle() == null || article.getTitle().isBlank()) {
            return false;
        }
        if (content.getBody() == null || content.getBody().isBlank()) {
            return false;
        }
        return content.getPublishStatus() != PublishStatus.PUBLISHED;
    }
}
