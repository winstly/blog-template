package cn.yanshisan.blog.content.infrastructure.persistence.mapper;

import cn.yanshisan.blog.content.domain.entity.ContentRevision;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ContentRevisionMapper extends BaseMapper<ContentRevision> {
}
