package cn.yanshisan.blog.classification.infrastructure.ohs;

import cn.yanshisan.blog.classification.domain.entity.ArticleTag;
import cn.yanshisan.blog.classification.domain.entity.Tag;
import cn.yanshisan.blog.classification.domain.repository.RelationRepository;
import cn.yanshisan.blog.classification.domain.repository.TagRepository;
import cn.yanshisan.blog.classification.domain.vo.RelationType;
import cn.yanshisan.blog.shared.ohs.TagOHS;
import cn.yanshisan.blog.shared.ohs.TagVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TagOHSImpl implements TagOHS {

    private final TagRepository tagRepository;
    private final RelationRepository relationRepository;

    @Override
    public List<TagVO> listCategoriesByArticle(String articleCode) {
        List<ArticleTag> relations = relationRepository.findByArticleCode(articleCode).stream()
                .filter(r -> r.getRelationType() == RelationType.CATEGORY)
                .toList();
        return relations.stream()
                .map(r -> tagRepository.findByCode(r.getTagCode()).orElse(null))
                .filter(tag -> tag != null)
                .map(tag -> new TagVO(tag.getTagCode(), tag.getTagName(), 0L))
                .toList();
    }

    @Override
    public List<TagVO> listTagsByArticle(String articleCode) {
        List<ArticleTag> relations = relationRepository.findByArticleCode(articleCode).stream()
                .filter(r -> r.getRelationType() == RelationType.TAG)
                .toList();
        return relations.stream()
                .map(r -> tagRepository.findByCode(r.getTagCode()).orElse(null))
                .filter(tag -> tag != null)
                .map(tag -> new TagVO(tag.getTagCode(), tag.getTagName(), 0L))
                .toList();
    }

    @Override
    public List<TagVO> listAllCategories() {
        List<ArticleTag> relations = relationRepository.findByRelationType(RelationType.CATEGORY);
        List<String> categoryTagCodes = relations.stream()
                .map(ArticleTag::getTagCode)
                .distinct()
                .toList();
        return categoryTagCodes.stream()
                .map(tagCode -> tagRepository.findByCode(tagCode).orElse(null))
                .filter(tag -> tag != null)
                .map(tag -> {
                    long count = relationRepository.countByTagCodeAndRelationType(tag.getTagCode(), RelationType.CATEGORY);
                    return new TagVO(tag.getTagCode(), tag.getTagName(), count);
                })
                .toList();
    }

    @Override
    public List<TagVO> listAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream()
                .map(tag -> {
                    long count = relationRepository.countByTagCodeAndRelationType(tag.getTagCode(), RelationType.TAG);
                    return new TagVO(tag.getTagCode(), tag.getTagName(), count);
                })
                .toList();
    }

    @Override
    public boolean existsTag(String tagCode) {
        return tagRepository.existsByTagCode(tagCode);
    }
}
