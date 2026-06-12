package com.winstly.blog.classification.domain.repository;

import com.winstly.blog.classification.domain.entity.ArticleTagDO;
import com.winstly.blog.classification.domain.enums.RelationType;

import java.util.List;

public interface RelationRepository {

    void save(ArticleTagDO articleTag);

    void delete(ArticleTagDO articleTag);

    List<ArticleTagDO> findByArticleCode(String articleCode);

    List<ArticleTagDO> findByTagCode(String tagCode);

    List<ArticleTagDO> findByRelationType(RelationType relationType);

    List<ArticleTagDO> findByTagCodeAndRelationType(String tagCode, RelationType relationType);

    boolean existsByArticleCodeAndTagCodeAndRelationType(String articleCode, String tagCode, RelationType relationType);

    long countByTagCodeAndRelationType(String tagCode, RelationType relationType);

    void logicalDeleteByArticleCode(String articleCode);

    void logicalDeleteByTagCode(String tagCode);

    void deleteByArticleCodeAndRelationType(String articleCode, RelationType relationType);
}
