package cn.yanshisan.blog.content.interfaces.dto;

import cn.yanshisan.blog.content.domain.entity.Article;
import cn.yanshisan.blog.content.domain.entity.Content;
import cn.yanshisan.blog.content.domain.vo.PublishStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArticleVO {

    private Long id;
    private String articleCode;
    private String title;
    private String summary;
    private String coverUrl;
    private Boolean isPinned;
    private Integer viewCount;
    private LocalDateTime publishedAt;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    private String body;
    private String contentFormat;
    private Integer wordCount;
    private Integer version;
    private PublishStatus publishStatus;

    private List<TagSummaryVO> tags;
    private TagSummaryVO category;

    public static ArticleVO from(Article article, Content content) {
        ArticleVO vo = new ArticleVO();
        vo.setId(article.getId());
        vo.setArticleCode(article.getArticleCode());
        vo.setTitle(article.getTitle());
        vo.setSummary(article.getSummary());
        vo.setCoverUrl(article.getCoverUrl());
        vo.setIsPinned(article.getIsPinned());
        vo.setViewCount(article.getViewCount());
        vo.setPublishedAt(article.getPublishedAt());
        vo.setGmtCreate(article.getGmtCreate());
        vo.setGmtModified(article.getGmtModified());

        if (content != null) {
            vo.setBody(content.getBody());
            vo.setContentFormat(content.getContentFormat());
            vo.setWordCount(content.getWordCount());
            vo.setVersion(content.getVersion());
            vo.setPublishStatus(content.getPublishStatus());
        }
        return vo;
    }

    public static ArticleVO from(Article article, Content content, List<TagSummaryVO> tags, TagSummaryVO category) {
        ArticleVO vo = from(article, content);
        vo.setTags(tags);
        vo.setCategory(category);
        return vo;
    }
}
