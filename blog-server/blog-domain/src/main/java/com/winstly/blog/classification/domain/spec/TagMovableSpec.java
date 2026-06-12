package com.winstly.blog.classification.domain.spec;

import com.winstly.blog.classification.domain.entity.TagDO;
import com.winstly.blog.shared.api.ErrorCode;
import com.winstly.blog.shared.exception.BusinessException;

public class TagMovableSpec {

    public static void checkMovable(TagDO tag, TagDO newParent) {
        if (newParent == null) {
            return;
        }
        if (tag.getId().equals(newParent.getId())) {
            throw new BusinessException(ErrorCode.TAG_NOT_MOVABLE, "Cannot move tag under itself");
        }
        if (newParent.isDescendantOf(tag)) {
            throw new BusinessException(ErrorCode.TAG_NOT_MOVABLE, "Cannot move tag under its own descendant");
        }
    }
}
