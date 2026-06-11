package cn.yanshisan.blog.shared.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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
