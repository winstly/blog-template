package cn.yanshisan.blog.content.infrastructure.persistence;

import cn.yanshisan.blog.content.domain.entity.Article;
import cn.yanshisan.blog.content.domain.entity.Content;
import cn.yanshisan.blog.content.domain.entity.ContentRevision;
import cn.yanshisan.blog.content.domain.repository.ArticleRepository;
import cn.yanshisan.blog.content.domain.vo.PublishStatus;
import cn.yanshisan.blog.content.infrastructure.persistence.mapper.ArticleMapper;
import cn.yanshisan.blog.content.infrastructure.persistence.mapper.ContentMapper;
import cn.yanshisan.blog.content.infrastructure.persistence.mapper.ContentRevisionMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepository {

    private final ArticleMapper articleMapper;
    private final ContentMapper contentMapper;
    private final ContentRevisionMapper revisionMapper;

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
        LambdaQueryWrapper<Content> contentWrapper = new LambdaQueryWrapper<Content>()
                .eq(Content::getTargetType, "article")
                .eq(Content::getPublishStatus, PublishStatus.PUBLISHED)
                .select(Content::getTargetCode);
        List<Content> publishedContents = contentMapper.selectList(contentWrapper);
        List<String> publishedCodes = publishedContents.stream()
                .map(Content::getTargetCode)
                .toList();

        if (publishedCodes.isEmpty()) {
            return List.of();
        }

        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<Article>()
                .in(Article::getArticleCode, publishedCodes)
                .orderByDesc(Article::getIsPinned)
                .orderByDesc(Article::getPublishedAt)
                .last("LIMIT " + pageSize + " OFFSET " + (page - 1) * pageSize);
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
}
