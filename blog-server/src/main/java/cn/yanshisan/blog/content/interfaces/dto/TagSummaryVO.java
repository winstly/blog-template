package cn.yanshisan.blog.content.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagSummaryVO {
    private String tagCode;
    private String tagName;
}
