package com.winstly.blog.interaction.interfaces.vo;

import com.winstly.blog.interaction.domain.entity.InteractionDO;
import com.winstly.blog.interaction.domain.enums.DisplayStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class InteractionVO {

    private Long id;
    private String userName;
    private String userAvatarUrl;
    private String content;
    private String location;
    private LocalDateTime date;
    private String replyTo;
    private String status;  // "pending" or "approved"
    private List<InteractionVO> replies;

    public static InteractionVO from(InteractionDO interaction) {
        InteractionVO vo = new InteractionVO();
        vo.setId(interaction.getId());
        vo.setUserName(interaction.getUserName());
        vo.setUserAvatarUrl(interaction.getUserAvatarUrl());
        vo.setContent(interaction.getRemark());
        vo.setLocation(interaction.getExtMeta());
        vo.setDate(interaction.getGmtCreate());
        vo.setStatus(interaction.getDisplayStatus() != null && interaction.getDisplayStatus() == DisplayStatus.APPROVED.getValue() ? DisplayStatus.APPROVED.getLabel() : DisplayStatus.PENDING.getLabel());
        return vo;
    }

    public static List<InteractionVO> buildTree(List<InteractionDO> roots, List<InteractionDO> allReplies) {
        Map<Long, InteractionDO> replyMap = allReplies.stream()
                .collect(Collectors.toMap(InteractionDO::getId, r -> r));

        List<InteractionVO> result = new ArrayList<>();
        for (InteractionDO root : roots) {
            InteractionVO rootVo = from(root);
            rootVo.setReplies(buildNestedReplies(root.getTreePath(), allReplies, replyMap));
            result.add(rootVo);
        }
        return result;
    }

    private static List<InteractionVO> buildNestedReplies(String parentPath, List<InteractionDO> allReplies,
                                                           Map<Long, InteractionDO> replyMap) {
        List<InteractionVO> nested = new ArrayList<>();
        for (InteractionDO reply : allReplies) {
            if (!isDirectChild(parentPath, reply.getTreePath())) {
                continue;
            }
            InteractionVO vo = from(reply);

            Long parentInteractionId = extractParentId(reply.getTreePath());
            if (parentInteractionId != null && replyMap.containsKey(parentInteractionId)) {
                vo.setReplyTo(replyMap.get(parentInteractionId).getUserName());
            }

            vo.setReplies(buildNestedReplies(reply.getTreePath(), allReplies, replyMap));
            nested.add(vo);
        }
        return nested;
    }

    private static boolean isDirectChild(String parentPath, String childPath) {
        if (!childPath.startsWith(parentPath)) {
            return false;
        }
        String suffix = childPath.substring(parentPath.length());
        if (suffix.isEmpty()) {
            return false;
        }
        String trimmed = suffix.endsWith("/") ? suffix.substring(0, suffix.length() - 1) : suffix;
        return !trimmed.contains("/");
    }

    private static Long extractParentId(String treePath) {
        String trimmed = treePath.endsWith("/") ? treePath.substring(0, treePath.length() - 1) : treePath;
        int lastSlash = trimmed.lastIndexOf('/');
        if (lastSlash < 0) {
            return null;
        }
        String parentSegment = trimmed.substring(0, lastSlash);
        int prevSlash = parentSegment.lastIndexOf('/');
        if (prevSlash < 0) {
            return null;
        }
        try {
            return Long.parseLong(parentSegment.substring(prevSlash + 1));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
