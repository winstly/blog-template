package com.winstly.blog.content.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum ContentFormat {

    MARKDOWN("markdown");

    @EnumValue
    private final String value;

    ContentFormat(String value) {
        this.value = value;
    }
}
