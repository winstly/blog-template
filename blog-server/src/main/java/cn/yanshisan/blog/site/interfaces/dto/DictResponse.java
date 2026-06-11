package cn.yanshisan.blog.site.interfaces.dto;

import cn.yanshisan.blog.site.domain.entity.SysDict;
import lombok.Data;

@Data
public class DictResponse {

    private Long id;
    private String bizCode;
    private String subCode;
    private String itemCode;
    private String itemLabel;
    private String itemValue;
    private String extData;
    private Integer sortOrder;
    private Integer displayStatus;
    private String remark;

    public static DictResponse from(SysDict dict) {
        DictResponse resp = new DictResponse();
        resp.setId(dict.getId());
        resp.setBizCode(dict.getBizCode());
        resp.setSubCode(dict.getSubCode());
        resp.setItemCode(dict.getItemCode());
        resp.setItemLabel(dict.getItemLabel());
        resp.setItemValue(dict.getItemValue());
        resp.setExtData(dict.getExtData());
        resp.setSortOrder(dict.getSortOrder());
        resp.setDisplayStatus(dict.getDisplayStatus());
        resp.setRemark(dict.getRemark());
        return resp;
    }
}
