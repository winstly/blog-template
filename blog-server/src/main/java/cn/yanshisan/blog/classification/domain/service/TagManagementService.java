package cn.yanshisan.blog.classification.domain.service;

import cn.yanshisan.blog.classification.domain.entity.Tag;
import cn.yanshisan.blog.classification.domain.event.TagCreatedEvent;
import cn.yanshisan.blog.classification.domain.event.TagDeletedEvent;
import cn.yanshisan.blog.classification.domain.event.TagMovedEvent;
import cn.yanshisan.blog.classification.domain.factory.TagFactory;
import cn.yanshisan.blog.classification.domain.repository.TagRepository;
import cn.yanshisan.blog.classification.domain.spec.TagMovableSpec;
import cn.yanshisan.blog.classification.domain.vo.TreeNode;
import cn.yanshisan.blog.shared.api.ErrorCode;
import cn.yanshisan.blog.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagManagementService {

    private final TagRepository tagRepository;
    private final TagFactory tagFactory;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Tag createTag(String tagCode, String tagName, String description, String parentTagCode) {
        if (tagRepository.existsByTagCode(tagCode)) {
            throw new BusinessException(ErrorCode.TAG_CODE_DUPLICATE, tagCode);
        }

        Tag tag;
        if (parentTagCode == null || parentTagCode.isBlank()) {
            tag = tagFactory.createRootTag(tagCode, tagName, description);
            tagRepository.save(tag);
            tag.initTreePath(tag.getId());
            tagRepository.update(tag);
        } else {
            Tag parent = findTagOrThrow(parentTagCode);
            tag = tagFactory.createChildTag(tagCode, tagName, description, parent);
            tagRepository.save(tag);
            tag.setTreePath(TreeNode.computeNewPath(parent.getTreePath(), tag.getId()));
            tagRepository.update(tag);
        }

        eventPublisher.publishEvent(new TagCreatedEvent(this, tag.getTagCode(), tag.getTagName()));
        return tag;
    }

    @Transactional
    public void updateMetadata(String tagCode, String tagName, String description, Integer displayStatus) {
        Tag tag = findTagOrThrow(tagCode);
        tag.updateMetadata(tagName, description, displayStatus);
        tagRepository.update(tag);
    }

    @Transactional
    public void moveTag(String tagCode, String newParentTagCode) {
        Tag tag = findTagOrThrow(tagCode);
        Tag newParent = newParentTagCode != null && !newParentTagCode.isBlank()
                ? findTagOrThrow(newParentTagCode) : null;

        TagMovableSpec.checkMovable(tag, newParent);

        String oldTreePath = tag.getTreePath();
        int oldDepth = tag.getTreeDepth();

        tag.moveTo(newParent);
        tagRepository.update(tag);

        int depthDelta = tag.getTreeDepth() - oldDepth;
        List<Tag> descendants = tagRepository.findByTreePathPrefix(oldTreePath);
        for (Tag descendant : descendants) {
            if (descendant.getId().equals(tag.getId())) {
                continue;
            }
            String newPath = TreeNode.replacePathPrefix(descendant.getTreePath(), oldTreePath, tag.getTreePath());
            descendant.setTreePath(newPath);
            descendant.setTreeDepth(descendant.getTreeDepth() + depthDelta);
            tagRepository.update(descendant);
        }

        eventPublisher.publishEvent(new TagMovedEvent(this, tagCode, oldTreePath, tag.getTreePath()));
    }

    @Transactional
    public void deleteTag(String tagCode) {
        Tag tag = findTagOrThrow(tagCode);
        tagRepository.delete(tag);
        eventPublisher.publishEvent(new TagDeletedEvent(this, tagCode));
    }

    private Tag findTagOrThrow(String tagCode) {
        return tagRepository.findByCode(tagCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.TAG_NOT_FOUND, tagCode));
    }
}
