package cn.yanshisan.blog.interaction.domain.vo;

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
        return new Target("article", articleCode);
    }

    public static Target site() {
        return new Target("site", "site");
    }
}
