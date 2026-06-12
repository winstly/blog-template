package com.winstly.blog.interaction.domain.valueobject;

import com.winstly.blog.shared.constants.TargetType;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Target {

    private final String targetType;
    private final String targetCode;

    private Target(String targetType, String targetCode) {
        this.targetType = targetType;
        this.targetCode = targetCode;
    }

    public static Target article(String articleCode) {
        return new Target(TargetType.ARTICLE, articleCode);
    }

    public static Target site() {
        return new Target(TargetType.SITE, TargetType.SITE);
    }
}
