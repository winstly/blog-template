package com.winstly.blog.classification.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum RelationType {

    CATEGORY("category"),
    TAG("tag");

    @EnumValue
    private final String value;

    RelationType(String value) {
        this.value = value;
    }
}
