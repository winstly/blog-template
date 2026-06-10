package cn.yanshisan.blog.interaction.infrastructure.persistence.mapper;

import cn.yanshisan.blog.interaction.domain.entity.Interaction;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InteractionMapper extends BaseMapper<Interaction> {
}
