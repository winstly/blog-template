package com.winstly.blog.content.domain.valueobject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

@Getter
@EqualsAndHashCode
public class ArticleCode {

    private static final String DEFAULT_SLUG = "article";

    private final String value;

    public ArticleCode(String value) {
        Validate.notBlank(value, "Article code must not be blank");
        this.value = value;
    }

    public static ArticleCode fromTitle(String title) {
        String slug = title.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("[\\s]+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
        if (StringUtils.isBlank(slug)) {
            slug = DEFAULT_SLUG;
        }
        String suffix = UUID.randomUUID().toString().substring(0, 4);
        return new ArticleCode(slug + "-" + suffix);
    }
}
