package cn.yanshisan.blog.classification.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreeNode {

    private String treePath;
    private Integer treeDepth;

    public static String computeNewPath(String parentPath, Long id) {
        if (parentPath == null || parentPath.isBlank()) {
            return "/" + id + "/";
        }
        return parentPath + id + "/";
    }

    public static int computeDepth(String treePath) {
        if (treePath == null || treePath.isBlank()) {
            return 0;
        }
        return treePath.split("/").length - 1;
    }

    public static String replacePathPrefix(String oldPath, String oldPrefix, String newPrefix) {
        if (oldPath == null || oldPrefix == null) {
            return oldPath;
        }
        return newPrefix + oldPath.substring(oldPrefix.length());
    }
}
