package cn.yanshisan.blog.classification.application;

import cn.yanshisan.blog.classification.domain.entity.ArticleTag;
import cn.yanshisan.blog.classification.domain.entity.Tag;
import cn.yanshisan.blog.classification.domain.repository.RelationRepository;
import cn.yanshisan.blog.classification.domain.repository.TagRepository;
import cn.yanshisan.blog.classification.domain.service.TagManagementService;
import cn.yanshisan.blog.classification.domain.vo.RelationType;
import cn.yanshisan.blog.classification.infrastructure.persistence.mapper.ArticleTagMapper;
import cn.yanshisan.blog.classification.interfaces.dto.TagVO;
import cn.yanshisan.blog.shared.api.ErrorCode;
import cn.yanshisan.blog.shared.exception.BusinessException;
import cn.yanshisan.blog.shared.ohs.ArticleOHS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
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
    private final ArticleTagMapper articleTagMapper;

    public TagVO createTag(String tagCode, String tagName, String description, String parentTagCode) {
        Tag tag = tagManagementService.createTag(tagCode, tagName, description, parentTagCode);
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
        Tag tag = findTagOrThrow(tagCode);
        long articleCount = relationRepository.countByTagCodeAndRelationType(tagCode, RelationType.TAG);
        return TagVO.from(tag, articleCount);
    }

    public List<TagVO> listAllTags() {
        List<Tag> tags = tagRepository.findAll();
        List<ArticleTag> tagRelations = relationRepository.findByRelationType(RelationType.TAG);
        Map<String, Long> countMap = tagRelations.stream()
                .collect(Collectors.groupingBy(ArticleTag::getTagCode, Collectors.counting()));
        return tags.stream()
                .map(tag -> TagVO.from(tag, countMap.getOrDefault(tag.getTagCode(), 0L)))
                .toList();
    }

    public List<TagVO> listAllCategories() {
        List<ArticleTag> relations = relationRepository.findByRelationType(RelationType.CATEGORY);
        List<String> categoryTagCodes = relations.stream()
                .map(ArticleTag::getTagCode)
                .distinct()
                .toList();

        if (categoryTagCodes.isEmpty()) {
            return List.of();
        }

        Map<String, Long> countMap = relations.stream()
                .collect(Collectors.groupingBy(ArticleTag::getTagCode, Collectors.counting()));

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
                .map(ArticleTag::getTagCode)
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
                .map(ArticleTag::getTagCode)
                .toList();
        return tagCodes.stream()
                .map(tagCode -> tagRepository.findByCode(tagCode).orElse(null))
                .filter(Objects::nonNull)
                .map(tag -> TagVO.from(tag, 0L))
                .toList();
    }

    public List<String> listArticlesByCategory(String tagCode) {
        List<ArticleTag> relations = relationRepository.findByTagCodeAndRelationType(tagCode, RelationType.CATEGORY);
        return relations.stream()
                .map(ArticleTag::getArticleCode)
                .filter(articleOHS::existsArticle)
                .toList();
    }

    private Tag findTagOrThrow(String tagCode) {
        return tagRepository.findByCode(tagCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.TAG_NOT_FOUND, tagCode));
    }

    @Transactional
    public void assignTagsToArticle(String articleCode, List<String> tagCodes, RelationType relationType) {
        // Delete existing relations for this articleCode + relationType
        LambdaQueryWrapper<ArticleTag> deleteWrapper = new LambdaQueryWrapper<ArticleTag>()
                .eq(ArticleTag::getArticleCode, articleCode)
                .eq(ArticleTag::getRelationType, relationType);
        articleTagMapper.delete(deleteWrapper);

        // Insert new relations
        if (tagCodes != null && !tagCodes.isEmpty()) {
            for (String tagCode : tagCodes) {
                ArticleTag relation = new ArticleTag();
                relation.setArticleCode(articleCode);
                relation.setTagCode(tagCode);
                relation.setRelationType(relationType);
                articleTagMapper.insert(relation);
            }
        }
    }
}
