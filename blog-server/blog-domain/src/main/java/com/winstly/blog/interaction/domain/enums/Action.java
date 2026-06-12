package com.winstly.blog.interaction.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum Action {

    COMMENT("comment"),
    MESSAGE("message"),
    LIKE("like");

    @EnumValue
    private final String value;

    Action(String value) {
        this.value = value;
    }

    public static Action fromValue(String value) {
        for (Action action : values()) {
            if (action.value.equals(value)) {
                return action;
            }
        }
        throw new IllegalArgumentException("Unknown action type: " + value);
    }
}
