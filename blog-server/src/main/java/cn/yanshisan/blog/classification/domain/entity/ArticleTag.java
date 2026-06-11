package cn.yanshisan.blog.classification.domain.entity;

import cn.yanshisan.blog.classification.domain.vo.RelationType;
import cn.yanshisan.blog.shared.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@TableName("blog_article_tag")
public class ArticleTag extends BaseEntity {

    private String articleCode;
    private String tagCode;
    private RelationType relationType;
}
