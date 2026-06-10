package cn.yanshisan.blog.interaction.interfaces.dto;

import cn.yanshisan.blog.interaction.domain.entity.Interaction;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class InteractionVO {

    private Long id;
    private String userName;
    private String userAvatarUrl;
    private String content;
    private String location;
    private LocalDateTime date;
    private String replyTo;
    private List<InteractionVO> replies;

    public static InteractionVO from(Interaction interaction) {
        InteractionVO vo = new InteractionVO();
        vo.setId(interaction.getId());
        vo.setUserName(interaction.getUserName());
        vo.setUserAvatarUrl(interaction.getUserAvatarUrl());
        vo.setContent(interaction.getRemark());
        vo.setLocation(interaction.getExtMeta());
        vo.setDate(interaction.getGmtCreate());
        return vo;
    }

    public static List<InteractionVO> buildTree(List<Interaction> roots, List<Interaction> allReplies) {
        Map<Long, Interaction> replyMap = allReplies.stream()
                .collect(Collectors.toMap(Interaction::getId, r -> r));

        List<InteractionVO> result = new ArrayList<>();
        for (Interaction root : roots) {
            InteractionVO rootVo = from(root);
            rootVo.setReplies(buildNestedReplies(root.getTreePath(), allReplies, replyMap));
            result.add(rootVo);
        }
        return result;
    }

    private static List<InteractionVO> buildNestedReplies(String parentPath, List<Interaction> allReplies,
                                                           Map<Long, Interaction> replyMap) {
        List<InteractionVO> nested = new ArrayList<>();
        for (Interaction reply : allReplies) {
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
