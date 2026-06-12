package com.winstly.blog.classification.application;

import com.winstly.blog.classification.domain.entity.ArticleTagDO;
import com.winstly.blog.classification.domain.entity.TagDO;
import com.winstly.blog.classification.domain.repository.RelationRepository;
import com.winstly.blog.classification.domain.repository.TagRepository;
import com.winstly.blog.classification.domain.service.TagManagementService;
import com.winstly.blog.classification.domain.enums.RelationType;
import com.winstly.blog.classification.interfaces.vo.TagVO;
import com.winstly.blog.shared.api.ErrorCode;
import com.winstly.blog.shared.exception.BusinessException;
import com.winstly.blog.shared.ohs.ArticleOHS;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagApplicationService {

    private final TagManagementService tagManagementService;
    private final TagRepository tagRepository;
    private final RelationRepository relationRepository;
    private final ArticleOHS articleOHS;

    public TagVO createTag(String tagCode, String tagName, String description, String parentTagCode) {
        TagDO tag = tagManagementService.createTag(tagCode, tagName, description, parentTagCode);
        return TagVO.from(tag, 0L);
    }

    public TagVO updateMetadata(String tagCode, String tagName, String description, Integer displayStatus) {
        tagManagementService.updateMetadata(tagCode, tagName, description, displayStatus);
        return getTagDetail(tagCode);
    }

    public TagVO moveTag(String tagCode, String newParentTagCode) {
        tagManagementService.moveTag(tagCode, newParentTagCode);
        return getTagDetail(tagCode);
    }

    public void deleteTag(String tagCode) {
        tagManagementService.deleteTag(tagCode);
    }

    public TagVO getTagDetail(String tagCode) {
        TagDO tag = findTagOrThrow(tagCode);
        long articleCount = relationRepository.countByTagCodeAndRelationType(tagCode, RelationType.TAG);
        return TagVO.from(tag, articleCount);
    }

    public List<TagVO> listAllTags() {
        List<TagDO> tags = tagRepository.findAll();
        List<ArticleTagDO> tagRelations = relationRepository.findByRelationType(RelationType.TAG);
        Map<String, Long> countMap = tagRelations.stream()
                .collect(Collectors.groupingBy(ArticleTagDO::getTagCode, Collectors.counting()));
        return tags.stream()
                .map(tag -> TagVO.from(tag, countMap.getOrDefault(tag.getTagCode(), 0L)))
                .toList();
    }

    public List<TagVO> listAllCategories() {
        List<ArticleTagDO> relations = relationRepository.findByRelationType(RelationType.CATEGORY);
        List<String> categoryTagCodes = relations.stream()
                .map(ArticleTagDO::getTagCode)
                .distinct()
                .toList();

        if (categoryTagCodes.isEmpty()) {
            return List.of();
        }

        Map<String, Long> countMap = relations.stream()
                .collect(Collectors.groupingBy(ArticleTagDO::getTagCode, Collectors.counting()));

        return categoryTagCodes.stream()
                .map(tagCode -> tagRepository.findByCode(tagCode)
                        .map(tag -> TagVO.from(tag, countMap.getOrDefault(tagCode, 0L)))
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }

    public List<TagVO> listCategoriesByArticle(String articleCode) {
        if (!articleOHS.existsArticle(articleCode)) {
            return List.of();
        }
        List<String> tagCodes = relationRepository.findByArticleCode(articleCode).stream()
                .filter(r -> r.getRelationType() == RelationType.CATEGORY)
                .map(ArticleTagDO::getTagCode)
                .toList();
        return tagCodes.stream()
                .map(tagCode -> tagRepository.findByCode(tagCode).orElse(null))
                .filter(Objects::nonNull)
                .map(tag -> TagVO.from(tag, 0L))
                .toList();
    }

    public List<TagVO> listTagsByArticle(String articleCode) {
        if (!articleOHS.existsArticle(articleCode)) {
            return List.of();
        }
        List<String> tagCodes = relationRepository.findByArticleCode(articleCode).stream()
                .filter(r -> r.getRelationType() == RelationType.TAG)
                .map(ArticleTagDO::getTagCode)
                .toList();
        return tagCodes.stream()
                .map(tagCode -> tagRepository.findByCode(tagCode).orElse(null))
                .filter(Objects::nonNull)
                .map(tag -> TagVO.from(tag, 0L))
                .toList();
    }

    public List<String> listArticlesByCategory(String tagCode) {
        List<ArticleTagDO> relations = relationRepository.findByTagCodeAndRelationType(tagCode, RelationType.CATEGORY);
        return relations.stream()
                .map(ArticleTagDO::getArticleCode)
                .filter(articleOHS::existsArticle)
                .toList();
    }

    private TagDO findTagOrThrow(String tagCode) {
        return tagRepository.findByCode(tagCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.TAG_NOT_FOUND, tagCode));
    }

    @Transactional
    public void assignTagsToArticle(String articleCode, List<String> tagCodes, RelationType relationType) {
        relationRepository.deleteByArticleCodeAndRelationType(articleCode, relationType);
        if (CollectionUtils.isNotEmpty(tagCodes)) {
            for (String tagCode : tagCodes) {
                ArticleTagDO relation = new ArticleTagDO();
                relation.setArticleCode(articleCode);
                relation.setTagCode(tagCode);
                relation.setRelationType(relationType);
                relationRepository.save(relation);
            }
        }
    }
}
