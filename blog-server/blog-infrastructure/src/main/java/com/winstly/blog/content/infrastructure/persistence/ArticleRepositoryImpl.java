package com.winstly.blog.content.infrastructure.persistence;

import com.winstly.blog.classification.domain.entity.ArticleTagDO;
import com.winstly.blog.classification.domain.enums.RelationType;
import com.winstly.blog.classification.domain.repository.RelationRepository;
import com.winstly.blog.content.domain.entity.ArticleDO;
import com.winstly.blog.content.domain.entity.ContentDO;
import com.winstly.blog.content.domain.entity.ContentRevisionDO;
import com.winstly.blog.content.domain.repository.ArticleRepository;
import com.winstly.blog.content.domain.enums.PublishStatus;
import com.winstly.blog.content.infrastructure.persistence.mapper.ArticleMapper;
import com.winstly.blog.content.infrastructure.persistence.mapper.ContentMapper;
import com.winstly.blog.content.infrastructure.persistence.mapper.ContentRevisionMapper;
import com.winstly.blog.shared.constants.TargetType;
import com.winstly.blog.shared.api.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepository {

    private final ArticleMapper articleMapper;
    private final ContentMapper contentMapper;
    private final ContentRevisionMapper revisionMapper;
    private final RelationRepository relationRepository;

    @Override
    @Transactional
    public void save(ArticleDO article, ContentDO content) {
        articleMapper.insert(article);
        contentMapper.insert(content);
    }

    @Override
    @Transactional
    public void update(ArticleDO article, ContentDO content) {
        articleMapper.updateById(article);
        contentMapper.updateById(content);
    }

    @Override
    public Optional<ArticleDO> findByCode(String articleCode) {
        LambdaQueryWrapper<ArticleDO> wrapper = new LambdaQueryWrapper<ArticleDO>()
                .eq(ArticleDO::getArticleCode, articleCode);
        return Optional.ofNullable(articleMapper.selectOne(wrapper));
    }

    @Override
    public Optional<ContentDO> findContentByArticleCode(String articleCode) {
        LambdaQueryWrapper<ContentDO> wrapper = new LambdaQueryWrapper<ContentDO>()
                .eq(ContentDO::getTargetType, TargetType.ARTICLE)
                .eq(ContentDO::getTargetCode, articleCode);
        return Optional.ofNullable(contentMapper.selectOne(wrapper));
    }

    @Override
    public List<ContentDO> findContentsByArticleCodes(List<String> articleCodes) {
        if (CollectionUtils.isEmpty(articleCodes)) {
            return List.of();
        }
        LambdaQueryWrapper<ContentDO> wrapper = new LambdaQueryWrapper<ContentDO>()
                .eq(ContentDO::getTargetType, TargetType.ARTICLE)
                .in(ContentDO::getTargetCode, articleCodes);
        return contentMapper.selectList(wrapper);
    }

    @Override
    public List<ContentRevisionDO> findRevisionsByArticleCode(String articleCode) {
        ContentDO content = findContentByArticleCode(articleCode).orElse(null);
        if (content == null) {
            return List.of();
        }
        LambdaQueryWrapper<ContentRevisionDO> wrapper = new LambdaQueryWrapper<ContentRevisionDO>()
                .eq(ContentRevisionDO::getContentId, content.getId())
                .orderByDesc(ContentRevisionDO::getVersion);
        return revisionMapper.selectList(wrapper);
    }

    @Override
    public Optional<ContentRevisionDO> findRevisionByVersion(String articleCode, int version) {
        ContentDO content = findContentByArticleCode(articleCode).orElse(null);
        if (content == null) {
            return Optional.empty();
        }
        LambdaQueryWrapper<ContentRevisionDO> wrapper = new LambdaQueryWrapper<ContentRevisionDO>()
                .eq(ContentRevisionDO::getContentId, content.getId())
                .eq(ContentRevisionDO::getVersion, version);
        return Optional.ofNullable(revisionMapper.selectOne(wrapper));
    }

    @Override
    @Transactional
    public void delete(ArticleDO article, ContentDO content) {
        articleMapper.deleteById(article.getId());
        contentMapper.deleteById(content.getId());
    }

    @Override
    public long countPublished() {
        LambdaQueryWrapper<ContentDO> wrapper = new LambdaQueryWrapper<ContentDO>()
                .eq(ContentDO::getTargetType, TargetType.ARTICLE)
                .eq(ContentDO::getPublishStatus, PublishStatus.PUBLISHED);
        return contentMapper.selectCount(wrapper);
    }

    @Override
    public List<ArticleDO> findPublished(int page, int pageSize) {
        Page<ContentDO> contentPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<ContentDO> contentWrapper = new LambdaQueryWrapper<ContentDO>()
                .eq(ContentDO::getTargetType, TargetType.ARTICLE)
                .eq(ContentDO::getPublishStatus, PublishStatus.PUBLISHED)
                .select(ContentDO::getTargetCode)
                .orderByDesc(ContentDO::getGmtModified);
        Page<ContentDO> result = contentMapper.selectPage(contentPage, contentWrapper);
        List<String> publishedCodes = result.getRecords().stream()
                .map(ContentDO::getTargetCode)
                .toList();

        if (publishedCodes.isEmpty()) {
            return List.of();
        }

        LambdaQueryWrapper<ArticleDO> articleWrapper = new LambdaQueryWrapper<ArticleDO>()
                .in(ArticleDO::getArticleCode, publishedCodes)
                .orderByDesc(ArticleDO::getIsPinned)
                .orderByDesc(ArticleDO::getPublishedAt);
        return articleMapper.selectList(articleWrapper);
    }

    @Override
    public boolean existsByArticleCode(String articleCode) {
        LambdaQueryWrapper<ArticleDO> wrapper = new LambdaQueryWrapper<ArticleDO>()
                .eq(ArticleDO::getArticleCode, articleCode);
        return articleMapper.selectCount(wrapper) > 0;
    }

    @Override
    public void saveRevision(ContentRevisionDO revision) {
        revisionMapper.insert(revision);
    }

    @Override
    public PageResult<ArticleDO> findByFilter(String keyword, String status, String categoryTagCode, int page, int pageSize) {
        LambdaQueryWrapper<ArticleDO> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotBlank(keyword)) {
            wrapper.and(w -> w.like(ArticleDO::getTitle, keyword).or().like(ArticleDO::getSummary, keyword));
        }

        if (StringUtils.isNotBlank(status)) {
            PublishStatus publishStatus = PublishStatus.valueOf(status);
            LambdaQueryWrapper<ContentDO> contentWrapper = new LambdaQueryWrapper<ContentDO>()
                    .eq(ContentDO::getTargetType, TargetType.ARTICLE)
                    .eq(ContentDO::getPublishStatus, publishStatus)
                    .select(ContentDO::getTargetCode);
            List<String> codes = contentMapper.selectList(contentWrapper).stream()
                    .map(ContentDO::getTargetCode)
                    .collect(Collectors.toList());
            if (codes.isEmpty()) {
                return new PageResult<>(List.of(), 0, page, pageSize);
            }
            wrapper.in(ArticleDO::getArticleCode, codes);
        }

        if (StringUtils.isNotBlank(categoryTagCode)) {
            List<String> codes = relationRepository.findByTagCodeAndRelationType(categoryTagCode, RelationType.CATEGORY).stream()
                    .map(ArticleTagDO::getArticleCode)
                    .collect(Collectors.toList());
            if (codes.isEmpty()) {
                return new PageResult<>(List.of(), 0, page, pageSize);
            }
            wrapper.in(ArticleDO::getArticleCode, codes);
        }

        wrapper.orderByDesc(ArticleDO::getIsPinned)
                .orderByDesc(ArticleDO::getGmtModified);

        Page<ArticleDO> pageParam = new Page<>(page, pageSize);
        Page<ArticleDO> result = articleMapper.selectPage(pageParam, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), page, pageSize);
    }
}
