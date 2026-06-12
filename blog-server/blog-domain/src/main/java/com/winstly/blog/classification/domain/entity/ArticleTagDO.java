package com.winstly.blog.classification.domain.entity;

import com.winstly.blog.classification.domain.enums.RelationType;
import com.winstly.blog.shared.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@TableName("blog_article_tag")
public class ArticleTagDO extends BaseEntity {

    private String articleCode;
    private String tagCode;
    private RelationType relationType;
}
