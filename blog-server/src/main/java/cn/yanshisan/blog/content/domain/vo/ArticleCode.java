package cn.yanshisan.blog.content.domain.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@EqualsAndHashCode
public class ArticleCode {

    private final String value;

    public ArticleCode(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Article code must not be blank");
        }
        this.value = value;
    }

    public static ArticleCode fromTitle(String title) {
        String slug = title.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("[\\s]+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
        if (slug.isBlank()) {
            slug = "article";
        }
        String suffix = UUID.randomUUID().toString().substring(0, 4);
        return new ArticleCode(slug + "-" + suffix);
    }
}
