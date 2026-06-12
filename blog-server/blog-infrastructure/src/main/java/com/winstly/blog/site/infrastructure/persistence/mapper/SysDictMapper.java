package com.winstly.blog.site.infrastructure.persistence.mapper;

import com.winstly.blog.site.domain.entity.SysDictDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysDictMapper extends BaseMapper<SysDictDO> {
}
