package cn.yanshisan.blog.classification.infrastructure.persistence;

import cn.yanshisan.blog.classification.domain.entity.ArticleTag;
import cn.yanshisan.blog.classification.domain.repository.RelationRepository;
import cn.yanshisan.blog.classification.domain.vo.RelationType;
import cn.yanshisan.blog.classification.infrastructure.persistence.mapper.ArticleTagMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RelationRepositoryImpl implements RelationRepository {

    private final ArticleTagMapper articleTagMapper;

    @Override
    public void save(ArticleTag articleTag) {
        articleTagMapper.insert(articleTag);
    }

    @Override
    public void delete(ArticleTag articleTag) {
        articleTagMapper.deleteById(articleTag.getId());
    }

    @Override
    public List<ArticleTag> findByArticleCode(String articleCode) {
        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<ArticleTag>()
                .eq(ArticleTag::getArticleCode, articleCode);
        return articleTagMapper.selectList(wrapper);
    }

    @Override
    public List<ArticleTag> findByTagCode(String tagCode) {
        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<ArticleTag>()
                .eq(ArticleTag::getTagCode, tagCode);
        return articleTagMapper.selectList(wrapper);
    }

    @Override
    public List<ArticleTag> findByRelationType(RelationType relationType) {
        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<ArticleTag>()
                .eq(ArticleTag::getRelationType, relationType);
        return articleTagMapper.selectList(wrapper);
    }

    @Override
    public List<ArticleTag> findByTagCodeAndRelationType(String tagCode, RelationType relationType) {
        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<ArticleTag>()
                .eq(ArticleTag::getTagCode, tagCode)
                .eq(ArticleTag::getRelationType, relationType);
        return articleTagMapper.selectList(wrapper);
    }

    @Override
    public boolean existsByArticleCodeAndTagCodeAndRelationType(String articleCode, String tagCode, RelationType relationType) {
        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<ArticleTag>()
                .eq(ArticleTag::getArticleCode, articleCode)
                .eq(ArticleTag::getTagCode, tagCode)
                .eq(ArticleTag::getRelationType, relationType);
        return articleTagMapper.selectCount(wrapper) > 0;
    }

    @Override
    public long countByTagCodeAndRelationType(String tagCode, RelationType relationType) {
        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<ArticleTag>()
                .eq(ArticleTag::getTagCode, tagCode)
                .eq(ArticleTag::getRelationType, relationType);
        return articleTagMapper.selectCount(wrapper);
    }

    @Override
    public void logicalDeleteByArticleCode(String articleCode) {
        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<ArticleTag>()
                .eq(ArticleTag::getArticleCode, articleCode);
        articleTagMapper.delete(wrapper);
    }

    @Override
    public void logicalDeleteByTagCode(String tagCode) {
        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<ArticleTag>()
                .eq(ArticleTag::getTagCode, tagCode);
        articleTagMapper.delete(wrapper);
    }
}
