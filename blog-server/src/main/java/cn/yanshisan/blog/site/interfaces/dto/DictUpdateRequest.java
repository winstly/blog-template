package cn.yanshisan.blog.site.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DictUpdateRequest {

    @NotBlank(message = "bizCode不能为空")
    private String bizCode;

    @NotBlank(message = "subCode不能为空")
    private String subCode;

    @NotBlank(message = "itemCode不能为空")
    private String itemCode;

    private String itemLabel;
    private String itemValue;
    private String extData;
    private Integer sortOrder;
    private Integer displayStatus;
    private String remark;
}
