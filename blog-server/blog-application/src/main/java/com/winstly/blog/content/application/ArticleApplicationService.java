package com.winstly.blog.content.application;

import com.winstly.blog.content.domain.entity.ArticleDO;
import com.winstly.blog.content.domain.entity.ContentDO;
import com.winstly.blog.content.domain.repository.ArticleRepository;
import com.winstly.blog.content.domain.service.ArticleManagementService;
import com.winstly.blog.content.domain.service.ArticlePublicationService;
import com.winstly.blog.content.interfaces.vo.ArticleVO;
import com.winstly.blog.content.interfaces.vo.RevisionVO;
import com.winstly.blog.content.interfaces.vo.TagSummaryVO;
import com.winstly.blog.shared.api.ErrorCode;
import com.winstly.blog.shared.api.PageResult;
import com.winstly.blog.shared.exception.BusinessException;
import com.winstly.blog.shared.ohs.TagDTO;
import com.winstly.blog.shared.ohs.TagOHS;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleApplicationService {

    private final ArticleManagementService managementService;
    private final ArticlePublicationService publicationService;
    private final ArticleRepository articleRepository;
    private final TagOHS tagOHS;

    public ArticleVO createArticle(String title, String summary, String coverUrl, String body, String contentFormat) {
        ArticleDO article = managementService.createArticle(title, summary, coverUrl, body, contentFormat);
        ContentDO content = findContentOrThrow(article.getArticleCode());
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
        ArticleDO article = articleRepository.findByCode(articleCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND, articleCode));
        ContentDO content = findContentOrThrow(articleCode);
        ArticleVO vo = ArticleVO.from(article, content);
        resolveTagsAndCategory(vo);
        return vo;
    }

    public PageResult<ArticleVO> listPublishedArticles(int page, int pageSize) {
        List<ArticleDO> articles = articleRepository.findPublished(page, pageSize);
        long total = articleRepository.countPublished();

        List<String> articleCodes = articles.stream()
                .map(ArticleDO::getArticleCode)
                .toList();
        Map<String, ContentDO> contentMap = articleRepository.findContentsByArticleCodes(articleCodes).stream()
                .collect(Collectors.toMap(ContentDO::getTargetCode, c -> c));

        List<ArticleVO> vos = articles.stream()
                .map(article -> ArticleVO.from(article, contentMap.get(article.getArticleCode())))
                .toList();
        resolveTagsAndCategoryBatch(vos);
        return new PageResult<>(vos, total, page, pageSize);
    }

    public PageResult<ArticleVO> listAllArticles(int page, int pageSize, String keyword, String status, String category) {
        PageResult<ArticleDO> articlePage = articleRepository.findByFilter(keyword, status, category, page, pageSize);
        List<ArticleDO> articles = articlePage.getList();
        long total = articlePage.getPagination().getTotal();

        List<String> articleCodes = articles.stream()
                .map(ArticleDO::getArticleCode)
                .toList();
        Map<String, ContentDO> contentMap = articleRepository.findContentsByArticleCodes(articleCodes).stream()
                .collect(Collectors.toMap(ContentDO::getTargetCode, c -> c));

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

    private ContentDO findContentOrThrow(String articleCode) {
        return articleRepository.findContentByArticleCode(articleCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND, articleCode));
    }

    private void resolveTagsAndCategory(ArticleVO vo) {
        List<TagDTO> tagDTOs = tagOHS.listTagsByArticle(vo.getArticleCode());
        vo.setTags(tagDTOs.stream()
                .map(t -> new TagSummaryVO(t.getTagCode(), t.getTagName()))
                .toList());

        List<TagDTO> catDTOs = tagOHS.listCategoriesByArticle(vo.getArticleCode());
        if (!catDTOs.isEmpty()) {
            vo.setCategory(new TagSummaryVO(catDTOs.get(0).getTagCode(), catDTOs.get(0).getTagName()));
        }
    }

    private void resolveTagsAndCategoryBatch(List<ArticleVO> vos) {
        if (CollectionUtils.isEmpty(vos)) {
            return;
        }
        for (ArticleVO vo : vos) {
            resolveTagsAndCategory(vo);
        }
    }
}
