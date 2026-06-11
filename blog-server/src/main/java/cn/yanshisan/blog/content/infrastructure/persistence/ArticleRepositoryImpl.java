package cn.yanshisan.blog.content.infrastructure.persistence;

import cn.yanshisan.blog.classification.domain.entity.ArticleTag;
import cn.yanshisan.blog.classification.domain.vo.RelationType;
import cn.yanshisan.blog.content.domain.entity.Article;
import cn.yanshisan.blog.content.domain.entity.Content;
import cn.yanshisan.blog.content.domain.entity.ContentRevision;
import cn.yanshisan.blog.content.domain.repository.ArticleRepository;
import cn.yanshisan.blog.content.domain.vo.PublishStatus;
import cn.yanshisan.blog.content.infrastructure.persistence.mapper.ArticleMapper;
import cn.yanshisan.blog.content.infrastructure.persistence.mapper.ContentMapper;
import cn.yanshisan.blog.content.infrastructure.persistence.mapper.ContentRevisionMapper;
import cn.yanshisan.blog.classification.infrastructure.persistence.mapper.ArticleTagMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
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
    private final ArticleTagMapper articleTagMapper;

    @Override
    @Transactional
    public void save(Article article, Content content) {
        articleMapper.insert(article);
        contentMapper.insert(content);
    }

    @Override
    @Transactional
    public void update(Article article, Content content) {
        articleMapper.updateById(article);
        contentMapper.updateById(content);
    }

    @Override
    public Optional<Article> findByCode(String articleCode) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<Article>()
                .eq(Article::getArticleCode, articleCode);
        return Optional.ofNullable(articleMapper.selectOne(wrapper));
    }

    @Override
    public Optional<Content> findContentByArticleCode(String articleCode) {
        LambdaQueryWrapper<Content> wrapper = new LambdaQueryWrapper<Content>()
                .eq(Content::getTargetType, "article")
                .eq(Content::getTargetCode, articleCode);
        return Optional.ofNullable(contentMapper.selectOne(wrapper));
    }

    @Override
    public List<Content> findContentsByArticleCodes(List<String> articleCodes) {
        if (articleCodes == null || articleCodes.isEmpty()) {
            return List.of();
        }
        LambdaQueryWrapper<Content> wrapper = new LambdaQueryWrapper<Content>()
                .eq(Content::getTargetType, "article")
                .in(Content::getTargetCode, articleCodes);
        return contentMapper.selectList(wrapper);
    }

    @Override
    public List<ContentRevision> findRevisionsByArticleCode(String articleCode) {
        Content content = findContentByArticleCode(articleCode).orElse(null);
        if (content == null) {
            return List.of();
        }
        LambdaQueryWrapper<ContentRevision> wrapper = new LambdaQueryWrapper<ContentRevision>()
                .eq(ContentRevision::getContentId, content.getId())
                .orderByDesc(ContentRevision::getVersion);
        return revisionMapper.selectList(wrapper);
    }

    @Override
    public Optional<ContentRevision> findRevisionByVersion(String articleCode, int version) {
        Content content = findContentByArticleCode(articleCode).orElse(null);
        if (content == null) {
            return Optional.empty();
        }
        LambdaQueryWrapper<ContentRevision> wrapper = new LambdaQueryWrapper<ContentRevision>()
                .eq(ContentRevision::getContentId, content.getId())
                .eq(ContentRevision::getVersion, version);
        return Optional.ofNullable(revisionMapper.selectOne(wrapper));
    }

    @Override
    @Transactional
    public void delete(Article article, Content content) {
        articleMapper.deleteById(article.getId());
        contentMapper.deleteById(content.getId());
    }

    @Override
    public long countPublished() {
        LambdaQueryWrapper<Content> wrapper = new LambdaQueryWrapper<Content>()
                .eq(Content::getTargetType, "article")
                .eq(Content::getPublishStatus, PublishStatus.PUBLISHED);
        return contentMapper.selectCount(wrapper);
    }

    @Override
    public List<Article> findPublished(int page, int pageSize) {
        Page<Content> contentPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Content> contentWrapper = new LambdaQueryWrapper<Content>()
                .eq(Content::getTargetType, "article")
                .eq(Content::getPublishStatus, PublishStatus.PUBLISHED)
                .select(Content::getTargetCode)
                .orderByDesc(Content::getGmtModified);
        Page<Content> result = contentMapper.selectPage(contentPage, contentWrapper);
        List<String> publishedCodes = result.getRecords().stream()
                .map(Content::getTargetCode)
                .toList();

        if (publishedCodes.isEmpty()) {
            return List.of();
        }

        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<Article>()
                .in(Article::getArticleCode, publishedCodes)
                .orderByDesc(Article::getIsPinned)
                .orderByDesc(Article::getPublishedAt);
        return articleMapper.selectList(articleWrapper);
    }

    @Override
    public boolean existsByArticleCode(String articleCode) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<Article>()
                .eq(Article::getArticleCode, articleCode);
        return articleMapper.selectCount(wrapper) > 0;
    }

    @Override
    public void saveRevision(ContentRevision revision) {
        revisionMapper.insert(revision);
    }

    @Override
    public Page<Article> findByFilter(String keyword, String status, String categoryTagCode, int page, int pageSize) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();

        // Keyword filter on title or summary
        if (keyword != null && !keyword.isBlank()) {
            wrapper.and(w -> w.like(Article::getTitle, keyword).or().like(Article::getSummary, keyword));
        }

        // Status filter via Content sub-query
        if (status != null && !status.isBlank()) {
            PublishStatus publishStatus = PublishStatus.valueOf(status);
            LambdaQueryWrapper<Content> contentWrapper = new LambdaQueryWrapper<Content>()
                    .eq(Content::getTargetType, "article")
                    .eq(Content::getPublishStatus, publishStatus)
                    .select(Content::getTargetCode);
            List<String> codes = contentMapper.selectList(contentWrapper).stream()
                    .map(Content::getTargetCode)
                    .collect(Collectors.toList());
            if (codes.isEmpty()) {
                return new Page<>(page, pageSize);
            }
            wrapper.in(Article::getArticleCode, codes);
        }

        // Category filter via ArticleTag sub-query
        if (categoryTagCode != null && !categoryTagCode.isBlank()) {
            LambdaQueryWrapper<ArticleTag> tagWrapper = new LambdaQueryWrapper<ArticleTag>()
                    .eq(ArticleTag::getTagCode, categoryTagCode)
                    .eq(ArticleTag::getRelationType, RelationType.CATEGORY)
                    .select(ArticleTag::getArticleCode);
            List<String> codes = articleTagMapper.selectList(tagWrapper).stream()
                    .map(ArticleTag::getArticleCode)
                    .collect(Collectors.toList());
            if (codes.isEmpty()) {
                return new Page<>(page, pageSize);
            }
            wrapper.in(Article::getArticleCode, codes);
        }

        wrapper.orderByDesc(Article::getIsPinned)
                .orderByDesc(Article::getGmtModified);

        Page<Article> pageParam = new Page<>(page, pageSize);
        return articleMapper.selectPage(pageParam, wrapper);
    }
}
