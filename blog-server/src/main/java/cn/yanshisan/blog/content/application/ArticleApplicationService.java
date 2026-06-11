package cn.yanshisan.blog.content.application;

import cn.yanshisan.blog.classification.domain.entity.ArticleTag;
import cn.yanshisan.blog.classification.domain.entity.Tag;
import cn.yanshisan.blog.classification.domain.vo.RelationType;
import cn.yanshisan.blog.classification.infrastructure.persistence.mapper.ArticleTagMapper;
import cn.yanshisan.blog.classification.infrastructure.persistence.mapper.TagMapper;
import cn.yanshisan.blog.content.domain.entity.Article;
import cn.yanshisan.blog.content.domain.entity.Content;
import cn.yanshisan.blog.content.domain.entity.ContentRevision;
import cn.yanshisan.blog.content.domain.repository.ArticleRepository;
import cn.yanshisan.blog.content.domain.service.ArticleManagementService;
import cn.yanshisan.blog.content.domain.service.ArticlePublicationService;
import cn.yanshisan.blog.content.interfaces.dto.ArticleVO;
import cn.yanshisan.blog.content.interfaces.dto.RevisionVO;
import cn.yanshisan.blog.content.interfaces.dto.TagSummaryVO;
import cn.yanshisan.blog.shared.api.ErrorCode;
import cn.yanshisan.blog.shared.api.PageResult;
import cn.yanshisan.blog.shared.exception.BusinessException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleApplicationService {

    private final ArticleManagementService managementService;
    private final ArticlePublicationService publicationService;
    private final ArticleRepository articleRepository;
    private final ArticleTagMapper articleTagMapper;
    private final TagMapper tagMapper;

    public ArticleVO createArticle(String title, String summary, String coverUrl, String body, String contentFormat) {
        Article article = managementService.createArticle(title, summary, coverUrl, body, contentFormat);
        Content content = findContentOrThrow(article.getArticleCode());
        return ArticleVO.from(article, content);
    }

    public ArticleVO updateMetadata(String articleCode, String title, String summary, String coverUrl) {
        managementService.updateMetadata(articleCode, title, summary, coverUrl);
        return getArticleDetail(articleCode);
    }

    public ArticleVO updateContent(String articleCode, String body) {
        managementService.updateContent(articleCode, body);
        return getArticleDetail(articleCode);
    }

    public ArticleVO publish(String articleCode) {
        publicationService.publish(articleCode);
        return getArticleDetail(articleCode);
    }

    public ArticleVO unpublish(String articleCode) {
        publicationService.unpublish(articleCode);
        return getArticleDetail(articleCode);
    }

    public ArticleVO togglePin(String articleCode) {
        managementService.togglePin(articleCode);
        return getArticleDetail(articleCode);
    }

    public void deleteArticle(String articleCode) {
        managementService.deleteArticle(articleCode);
    }

    public void deleteArticles(List<String> articleCodes) {
        managementService.deleteArticles(articleCodes);
    }

    public ArticleVO getArticleDetail(String articleCode) {
        Article article = articleRepository.findByCode(articleCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND, articleCode));
        Content content = findContentOrThrow(articleCode);
        ArticleVO vo = ArticleVO.from(article, content);
        resolveTagsAndCategory(vo);
        return vo;
    }

    public PageResult<ArticleVO> listPublishedArticles(int page, int pageSize) {
        List<Article> articles = articleRepository.findPublished(page, pageSize);
        long total = articleRepository.countPublished();

        List<String> articleCodes = articles.stream()
                .map(Article::getArticleCode)
                .toList();
        Map<String, Content> contentMap = articleRepository.findContentsByArticleCodes(articleCodes).stream()
                .collect(Collectors.toMap(Content::getTargetCode, c -> c));

        List<ArticleVO> vos = articles.stream()
                .map(article -> ArticleVO.from(article, contentMap.get(article.getArticleCode())))
                .toList();
        resolveTagsAndCategoryBatch(vos);
        return new PageResult<>(vos, total, page, pageSize);
    }

    public PageResult<ArticleVO> listAllArticles(int page, int pageSize, String keyword, String status, String category) {
        Page<Article> articlePage = articleRepository.findByFilter(keyword, status, category, page, pageSize);
        List<Article> articles = articlePage.getRecords();
        long total = articlePage.getTotal();

        List<String> articleCodes = articles.stream()
                .map(Article::getArticleCode)
                .toList();
        Map<String, Content> contentMap = articleRepository.findContentsByArticleCodes(articleCodes).stream()
                .collect(Collectors.toMap(Content::getTargetCode, c -> c));

        List<ArticleVO> vos = articles.stream()
                .map(article -> ArticleVO.from(article, contentMap.get(article.getArticleCode())))
                .toList();
        resolveTagsAndCategoryBatch(vos);
        return new PageResult<>(vos, total, page, pageSize);
    }

    public List<RevisionVO> listRevisions(String articleCode) {
        return articleRepository.findRevisionsByArticleCode(articleCode).stream()
                .map(RevisionVO::from)
                .toList();
    }

    public RevisionVO getRevision(String articleCode, int version) {
        return articleRepository.findRevisionByVersion(articleCode, version)
                .map(RevisionVO::from)
                .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND, articleCode));
    }

    private Content findContentOrThrow(String articleCode) {
        return articleRepository.findContentByArticleCode(articleCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND, articleCode));
    }

    private void resolveTagsAndCategory(ArticleVO vo) {
        LambdaQueryWrapper<ArticleTag> tagWrapper = new LambdaQueryWrapper<ArticleTag>()
                .eq(ArticleTag::getArticleCode, vo.getArticleCode());
        List<ArticleTag> relations = articleTagMapper.selectList(tagWrapper);

        List<String> tagCodes = relations.stream()
                .map(ArticleTag::getTagCode)
                .distinct()
                .toList();
        if (tagCodes.isEmpty()) {
            return;
        }

        Map<String, Tag> tagMap = new HashMap<>();
        LambdaQueryWrapper<Tag> tagQuery = new LambdaQueryWrapper<Tag>()
                .in(Tag::getTagCode, tagCodes);
        tagMapper.selectList(tagQuery).forEach(tag -> tagMap.put(tag.getTagCode(), tag));

        List<TagSummaryVO> tags = relations.stream()
                .filter(r -> r.getRelationType() == RelationType.TAG)
                .map(r -> {
                    Tag tag = tagMap.get(r.getTagCode());
                    return tag != null ? new TagSummaryVO(tag.getTagCode(), tag.getTagName()) : null;
                })
                .filter(Objects::nonNull)
                .toList();
        vo.setTags(tags);

        relations.stream()
                .filter(r -> r.getRelationType() == RelationType.CATEGORY)
                .findFirst()
                .ifPresent(r -> {
                    Tag tag = tagMap.get(r.getTagCode());
                    if (tag != null) {
                        vo.setCategory(new TagSummaryVO(tag.getTagCode(), tag.getTagName()));
                    }
                });
    }

    private void resolveTagsAndCategoryBatch(List<ArticleVO> vos) {
        if (vos == null || vos.isEmpty()) {
            return;
        }

        List<String> articleCodes = vos.stream()
                .map(ArticleVO::getArticleCode)
                .toList();

        LambdaQueryWrapper<ArticleTag> tagWrapper = new LambdaQueryWrapper<ArticleTag>()
                .in(ArticleTag::getArticleCode, articleCodes);
        List<ArticleTag> allRelations = articleTagMapper.selectList(tagWrapper);

        List<String> tagCodes = allRelations.stream()
                .map(ArticleTag::getTagCode)
                .distinct()
                .toList();
        if (tagCodes.isEmpty()) {
            return;
        }

        Map<String, Tag> tagMap = new HashMap<>();
        LambdaQueryWrapper<Tag> tagQuery = new LambdaQueryWrapper<Tag>()
                .in(Tag::getTagCode, tagCodes);
        tagMapper.selectList(tagQuery).forEach(tag -> tagMap.put(tag.getTagCode(), tag));

        Map<String, List<ArticleTag>> relationsByArticle = allRelations.stream()
                .collect(Collectors.groupingBy(ArticleTag::getArticleCode));

        for (ArticleVO vo : vos) {
            List<ArticleTag> articleRelations = relationsByArticle.getOrDefault(vo.getArticleCode(), List.of());

            List<TagSummaryVO> tags = articleRelations.stream()
                    .filter(r -> r.getRelationType() == RelationType.TAG)
                    .map(r -> {
                        Tag tag = tagMap.get(r.getTagCode());
                        return tag != null ? new TagSummaryVO(tag.getTagCode(), tag.getTagName()) : null;
                    })
                    .filter(Objects::nonNull)
                    .toList();
            vo.setTags(tags);

            articleRelations.stream()
                    .filter(r -> r.getRelationType() == RelationType.CATEGORY)
                    .findFirst()
                    .ifPresent(r -> {
                        Tag tag = tagMap.get(r.getTagCode());
                        if (tag != null) {
                            vo.setCategory(new TagSummaryVO(tag.getTagCode(), tag.getTagName()));
                        }
                    });
        }
    }
}
