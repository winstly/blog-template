package cn.yanshisan.blog.classification.infrastructure.persistence.mapper;

import cn.yanshisan.blog.classification.domain.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {
}
