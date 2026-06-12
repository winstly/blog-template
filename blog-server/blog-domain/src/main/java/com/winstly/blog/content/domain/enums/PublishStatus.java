package com.winstly.blog.content.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum PublishStatus {

    DRAFT(0),
    PUBLISHED(1);

    @EnumValue
    private final int value;

    PublishStatus(int value) {
        this.value = value;
    }

    public static PublishStatus fromValue(int value) {
        for (PublishStatus status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown publish status value: " + value);
    }
}
