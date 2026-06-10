package cn.yanshisan.blog.site.infrastructure.persistence.mapper;

import cn.yanshisan.blog.site.domain.entity.SysDict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysDictMapper extends BaseMapper<SysDict> {
}
