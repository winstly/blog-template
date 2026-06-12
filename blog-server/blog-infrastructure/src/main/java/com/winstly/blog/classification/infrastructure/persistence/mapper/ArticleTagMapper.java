package com.winstly.blog.classification.infrastructure.persistence.mapper;

import com.winstly.blog.classification.domain.entity.ArticleTagDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleTagMapper extends BaseMapper<ArticleTagDO> {
}
