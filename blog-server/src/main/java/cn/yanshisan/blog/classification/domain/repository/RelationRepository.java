package cn.yanshisan.blog.classification.domain.repository;

import cn.yanshisan.blog.classification.domain.entity.ArticleTag;
import cn.yanshisan.blog.classification.domain.vo.RelationType;

import java.util.List;

public interface RelationRepository {

    void save(ArticleTag articleTag);

    void delete(ArticleTag articleTag);

    List<ArticleTag> findByArticleCode(String articleCode);

    List<ArticleTag> findByTagCode(String tagCode);

    List<ArticleTag> findByRelationType(RelationType relationType);

    List<ArticleTag> findByTagCodeAndRelationType(String tagCode, RelationType relationType);

    boolean existsByArticleCodeAndTagCodeAndRelationType(String articleCode, String tagCode, RelationType relationType);

    long countByTagCodeAndRelationType(String tagCode, RelationType relationType);

    void logicalDeleteByArticleCode(String articleCode);

    void logicalDeleteByTagCode(String tagCode);
}
