package com.winstly.blog.interaction.infrastructure.persistence.mapper;

import com.winstly.blog.interaction.domain.entity.InteractionDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InteractionMapper extends BaseMapper<InteractionDO> {
}
