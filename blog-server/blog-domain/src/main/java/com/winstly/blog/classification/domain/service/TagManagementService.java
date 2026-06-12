package com.winstly.blog.classification.domain.service;

import com.winstly.blog.classification.domain.entity.TagDO;
import com.winstly.blog.classification.domain.event.TagCreatedEvent;
import com.winstly.blog.classification.domain.event.TagDeletedEvent;
import com.winstly.blog.classification.domain.event.TagMovedEvent;
import com.winstly.blog.classification.domain.factory.TagFactory;
import com.winstly.blog.classification.domain.repository.TagRepository;
import com.winstly.blog.classification.domain.spec.TagMovableSpec;
import com.winstly.blog.classification.domain.valueobject.TreeNode;
import com.winstly.blog.shared.api.ErrorCode;
import com.winstly.blog.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
    public TagDO createTag(String tagCode, String tagName, String description, String parentTagCode) {
        if (tagRepository.existsByTagCode(tagCode)) {
            throw new BusinessException(ErrorCode.TAG_CODE_DUPLICATE, tagCode);
        }

        TagDO tag;
        if (StringUtils.isBlank(parentTagCode)) {
            tag = tagFactory.createRootTag(tagCode, tagName, description);
            tagRepository.save(tag);
            tag.initTreePath(tag.getId());
            tagRepository.update(tag);
        } else {
            TagDO parent = findTagOrThrow(parentTagCode);
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
        TagDO tag = findTagOrThrow(tagCode);
        tag.updateMetadata(tagName, description, displayStatus);
        tagRepository.update(tag);
    }

    @Transactional
    public void moveTag(String tagCode, String newParentTagCode) {
        TagDO tag = findTagOrThrow(tagCode);
        TagDO newParent = StringUtils.isNotBlank(newParentTagCode)
                ? findTagOrThrow(newParentTagCode) : null;

        TagMovableSpec.checkMovable(tag, newParent);

        String oldTreePath = tag.getTreePath();
        int oldDepth = tag.getTreeDepth();

        tag.moveTo(newParent);
        tagRepository.update(tag);

        int depthDelta = tag.getTreeDepth() - oldDepth;
        List<TagDO> descendants = tagRepository.findByTreePathPrefix(oldTreePath);
        for (TagDO descendant : descendants) {
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
        TagDO tag = findTagOrThrow(tagCode);
        List<TagDO> descendants = tagRepository.findByTreePathPrefix(tag.getTreePath());
        for (TagDO descendant : descendants) {
            tagRepository.delete(descendant);
            eventPublisher.publishEvent(new TagDeletedEvent(this, descendant.getTagCode()));
        }
    }

    private TagDO findTagOrThrow(String tagCode) {
        return tagRepository.findByCode(tagCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.TAG_NOT_FOUND, tagCode));
    }
}
