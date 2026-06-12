package com.winstly.blog.content.interfaces.vo;

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
