package com.winstly.blog.classification.infrastructure.persistence;

import com.winstly.blog.classification.domain.entity.ArticleTagDO;
import com.winstly.blog.classification.domain.repository.RelationRepository;
import com.winstly.blog.classification.domain.enums.RelationType;
import com.winstly.blog.classification.infrastructure.persistence.mapper.ArticleTagMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RelationRepositoryImpl implements RelationRepository {

    private final ArticleTagMapper articleTagMapper;

    @Override
    public void save(ArticleTagDO articleTag) {
        articleTagMapper.insert(articleTag);
    }

    @Override
    public void delete(ArticleTagDO articleTag) {
        articleTagMapper.deleteById(articleTag.getId());
    }

    @Override
    public List<ArticleTagDO> findByArticleCode(String articleCode) {
        LambdaQueryWrapper<ArticleTagDO> wrapper = new LambdaQueryWrapper<ArticleTagDO>()
                .eq(ArticleTagDO::getArticleCode, articleCode);
        return articleTagMapper.selectList(wrapper);
    }

    @Override
    public List<ArticleTagDO> findByTagCode(String tagCode) {
        LambdaQueryWrapper<ArticleTagDO> wrapper = new LambdaQueryWrapper<ArticleTagDO>()
                .eq(ArticleTagDO::getTagCode, tagCode);
        return articleTagMapper.selectList(wrapper);
    }

    @Override
    public List<ArticleTagDO> findByRelationType(RelationType relationType) {
        LambdaQueryWrapper<ArticleTagDO> wrapper = new LambdaQueryWrapper<ArticleTagDO>()
                .eq(ArticleTagDO::getRelationType, relationType);
        return articleTagMapper.selectList(wrapper);
    }

    @Override
    public List<ArticleTagDO> findByTagCodeAndRelationType(String tagCode, RelationType relationType) {
        LambdaQueryWrapper<ArticleTagDO> wrapper = new LambdaQueryWrapper<ArticleTagDO>()
                .eq(ArticleTagDO::getTagCode, tagCode)
                .eq(ArticleTagDO::getRelationType, relationType);
        return articleTagMapper.selectList(wrapper);
    }

    @Override
    public boolean existsByArticleCodeAndTagCodeAndRelationType(String articleCode, String tagCode, RelationType relationType) {
        LambdaQueryWrapper<ArticleTagDO> wrapper = new LambdaQueryWrapper<ArticleTagDO>()
                .eq(ArticleTagDO::getArticleCode, articleCode)
                .eq(ArticleTagDO::getTagCode, tagCode)
                .eq(ArticleTagDO::getRelationType, relationType);
        return articleTagMapper.selectCount(wrapper) > 0;
    }

    @Override
    public long countByTagCodeAndRelationType(String tagCode, RelationType relationType) {
        LambdaQueryWrapper<ArticleTagDO> wrapper = new LambdaQueryWrapper<ArticleTagDO>()
                .eq(ArticleTagDO::getTagCode, tagCode)
                .eq(ArticleTagDO::getRelationType, relationType);
        return articleTagMapper.selectCount(wrapper);
    }

    @Override
    public void logicalDeleteByArticleCode(String articleCode) {
        LambdaUpdateWrapper<ArticleTagDO> wrapper = new LambdaUpdateWrapper<ArticleTagDO>()
                .eq(ArticleTagDO::getArticleCode, articleCode)
                .set(ArticleTagDO::getIsDeleted, 1);
        articleTagMapper.update(null, wrapper);
    }

    @Override
    public void logicalDeleteByTagCode(String tagCode) {
        LambdaUpdateWrapper<ArticleTagDO> wrapper = new LambdaUpdateWrapper<ArticleTagDO>()
                .eq(ArticleTagDO::getTagCode, tagCode)
                .set(ArticleTagDO::getIsDeleted, 1);
        articleTagMapper.update(null, wrapper);
    }

    @Override
    public void deleteByArticleCodeAndRelationType(String articleCode, RelationType relationType) {
        LambdaUpdateWrapper<ArticleTagDO> wrapper = new LambdaUpdateWrapper<ArticleTagDO>()
                .eq(ArticleTagDO::getArticleCode, articleCode)
                .eq(ArticleTagDO::getRelationType, relationType)
                .set(ArticleTagDO::getIsDeleted, 1);
        articleTagMapper.update(null, wrapper);
    }
}
