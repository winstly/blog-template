package cn.yanshisan.blog.site.domain.entity;

import cn.yanshisan.blog.shared.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict")
public class SysDict extends BaseEntity {

    private String bizCode;
    private String subCode;
    private String itemCode;
    private String itemLabel;
    private String itemValue;
    private String extData;
    private Integer sortOrder;
    private Integer displayStatus;
    private String remark;
}
