package cn.yanshisan.blog.classification.application;

import cn.yanshisan.blog.classification.domain.entity.ArticleTag;
import cn.yanshisan.blog.classification.domain.entity.Tag;
import cn.yanshisan.blog.classification.domain.repository.RelationRepository;
import cn.yanshisan.blog.classification.domain.repository.TagRepository;
import cn.yanshisan.blog.classification.domain.service.TagManagementService;
import cn.yanshisan.blog.classification.domain.vo.RelationType;
import cn.yanshisan.blog.classification.interfaces.dto.TagVO;
import cn.yanshisan.blog.shared.ohs.ArticleOHS;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagApplicationService {

    private final TagManagementService tagManagementService;
    private final TagRepository tagRepository;
    private final RelationRepository relationRepository;
    private final ArticleOHS articleOHS;

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
        Tag tag = tagRepository.findByCode(tagCode)
                .orElseThrow();
        long articleCount = relationRepository.countByTagCodeAndRelationType(tagCode, RelationType.TAG);
        return TagVO.from(tag, articleCount);
    }

    public List<TagVO> listAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream()
                .map(tag -> {
                    long count = relationRepository.countByTagCodeAndRelationType(tag.getTagCode(), RelationType.TAG);
                    return TagVO.from(tag, count);
                })
                .toList();
    }

    public List<TagVO> listAllCategories() {
        List<ArticleTag> relations = relationRepository.findByRelationType(RelationType.CATEGORY);
        List<String> categoryTagCodes = relations.stream()
                .map(ArticleTag::getTagCode)
                .distinct()
                .toList();

        return categoryTagCodes.stream()
                .map(tagCode -> {
                    Tag tag = tagRepository.findByCode(tagCode).orElse(null);
                    if (tag == null) return null;
                    long count = relationRepository.countByTagCodeAndRelationType(tagCode, RelationType.CATEGORY);
                    return TagVO.from(tag, count);
                })
                .filter(vo -> vo != null)
                .toList();
    }

    public List<TagVO> listCategoriesByArticle(String articleCode) {
        if (!articleOHS.existsArticle(articleCode)) {
            return List.of();
        }
        List<ArticleTag> relations = relationRepository.findByArticleCode(articleCode).stream()
                .filter(r -> r.getRelationType() == RelationType.CATEGORY)
                .toList();
        return relations.stream()
                .map(r -> tagRepository.findByCode(r.getTagCode()).orElse(null))
                .filter(tag -> tag != null)
                .map(tag -> TagVO.from(tag, 0L))
                .toList();
    }

    public List<TagVO> listTagsByArticle(String articleCode) {
        if (!articleOHS.existsArticle(articleCode)) {
            return List.of();
        }
        List<ArticleTag> relations = relationRepository.findByArticleCode(articleCode).stream()
                .filter(r -> r.getRelationType() == RelationType.TAG)
                .toList();
        return relations.stream()
                .map(r -> tagRepository.findByCode(r.getTagCode()).orElse(null))
                .filter(tag -> tag != null)
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
}
