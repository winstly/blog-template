package com.winstly.blog.content.infrastructure.persistence.mapper;

import com.winstly.blog.content.domain.entity.ArticleDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper extends BaseMapper<ArticleDO> {
}
