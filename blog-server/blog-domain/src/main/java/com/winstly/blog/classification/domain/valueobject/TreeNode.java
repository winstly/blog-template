package com.winstly.blog.classification.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreeNode {

    private String treePath;
    private Integer treeDepth;

    public static String computeNewPath(String parentPath, Long id) {
        if (StringUtils.isBlank(parentPath)) {
            return "/" + id + "/";
        }
        return parentPath + id + "/";
    }

    public static int computeDepth(String treePath) {
        if (StringUtils.isBlank(treePath)) {
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
