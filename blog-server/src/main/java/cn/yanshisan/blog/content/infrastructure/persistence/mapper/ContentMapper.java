package cn.yanshisan.blog.content.infrastructure.persistence.mapper;

import cn.yanshisan.blog.content.domain.entity.Content;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ContentMapper extends BaseMapper<Content> {
}
