package com.winstly.blog.classification.infrastructure.ohs;

import com.winstly.blog.classification.domain.entity.ArticleTagDO;
import com.winstly.blog.classification.domain.entity.TagDO;
import com.winstly.blog.classification.domain.repository.RelationRepository;
import com.winstly.blog.classification.domain.repository.TagRepository;
import com.winstly.blog.classification.domain.enums.RelationType;
import com.winstly.blog.shared.ohs.TagOHS;
import com.winstly.blog.shared.ohs.TagDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TagOHSImpl implements TagOHS {

    private final TagRepository tagRepository;
    private final RelationRepository relationRepository;

    @Override
    public List<TagDTO> listCategoriesByArticle(String articleCode) {
        List<ArticleTagDO> relations = relationRepository.findByArticleCode(articleCode).stream()
                .filter(r -> r.getRelationType() == RelationType.CATEGORY)
                .toList();
        return relations.stream()
                .map(r -> tagRepository.findByCode(r.getTagCode()).orElse(null))
                .filter(tag -> tag != null)
                .map(tag -> new TagDTO(tag.getTagCode(), tag.getTagName(), 0L))
                .toList();
    }

    @Override
    public List<TagDTO> listTagsByArticle(String articleCode) {
        List<ArticleTagDO> relations = relationRepository.findByArticleCode(articleCode).stream()
                .filter(r -> r.getRelationType() == RelationType.TAG)
                .toList();
        return relations.stream()
                .map(r -> tagRepository.findByCode(r.getTagCode()).orElse(null))
                .filter(tag -> tag != null)
                .map(tag -> new TagDTO(tag.getTagCode(), tag.getTagName(), 0L))
                .toList();
    }

    @Override
    public List<TagDTO> listAllCategories() {
        List<ArticleTagDO> relations = relationRepository.findByRelationType(RelationType.CATEGORY);
        List<String> categoryTagCodes = relations.stream()
                .map(ArticleTagDO::getTagCode)
                .distinct()
                .toList();
        return categoryTagCodes.stream()
                .map(tagCode -> tagRepository.findByCode(tagCode).orElse(null))
                .filter(tag -> tag != null)
                .map(tag -> {
                    long count = relationRepository.countByTagCodeAndRelationType(tag.getTagCode(), RelationType.CATEGORY);
                    return new TagDTO(tag.getTagCode(), tag.getTagName(), count);
                })
                .toList();
    }

    @Override
    public List<TagDTO> listAllTags() {
        List<TagDO> tags = tagRepository.findAll();
        return tags.stream()
                .map(tag -> {
                    long count = relationRepository.countByTagCodeAndRelationType(tag.getTagCode(), RelationType.TAG);
                    return new TagDTO(tag.getTagCode(), tag.getTagName(), count);
                })
                .toList();
    }

    @Override
    public boolean existsTag(String tagCode) {
        return tagRepository.existsByTagCode(tagCode);
    }
}
