package cn.yanshisan.blog.classification.domain.spec;

import cn.yanshisan.blog.classification.domain.entity.Tag;
import cn.yanshisan.blog.shared.api.ErrorCode;
import cn.yanshisan.blog.shared.exception.BusinessException;

public class TagMovableSpec {

    public static void checkMovable(Tag tag, Tag newParent) {
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
