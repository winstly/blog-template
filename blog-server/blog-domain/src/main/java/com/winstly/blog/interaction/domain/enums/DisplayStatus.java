package com.winstly.blog.interaction.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum DisplayStatus {

    PENDING(0),
    APPROVED(1);

    @EnumValue
    private final int value;

    DisplayStatus(int value) {
        this.value = value;
    }

    public static DisplayStatus fromValue(int value) {
        for (DisplayStatus status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown display status value: " + value);
    }

    public String getLabel() {
        return this == APPROVED ? "approved" : "pending";
    }

    public static DisplayStatus fromLabel(String label) {
        if ("approved".equals(label)) {
            return APPROVED;
        }
        return PENDING;
    }
}
