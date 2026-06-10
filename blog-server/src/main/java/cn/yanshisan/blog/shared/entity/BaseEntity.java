package cn.yanshisan.blog.shared.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String creator;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    private String modifier;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

    @TableLogic
    private Integer isDeleted;
}
