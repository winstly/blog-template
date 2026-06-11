package cn.yanshisan.blog.shared.api;

import lombok.Getter;

@Getter
public enum ErrorCode {

    PARAM_ERROR(1000, "参数错误"),
    PARAM_MISSING(1001, "必填参数缺失"),
    PARAM_INVALID(1002, "参数格式不正确"),

    ARTICLE_NOT_FOUND(2001, "文章不存在"),
    ARTICLE_ALREADY_PUBLISHED(2002, "文章已发布"),
    ARTICLE_NOT_PUBLISHABLE(2003, "文章不满足发布条件"),
    TAG_NOT_FOUND(2010, "标签不存在"),
    TAG_CODE_DUPLICATE(2011, "标签编码重复"),
    TAG_NOT_MOVABLE(2012, "标签不可移动到目标位置"),
    INTERACTION_TARGET_NOT_FOUND(2020, "交互目标不存在"),
    INTERACTION_NOT_FOUND(2021, "交互不存在"),
    DICT_NOT_FOUND(2030, "字典不存在"),
    DICT_TRIPLE_DUPLICATE(2031, "字典三元组重复"),
    DIARY_NOT_FOUND(2040, "日记不存在"),

    SYSTEM_ERROR(3000, "系统错误");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
