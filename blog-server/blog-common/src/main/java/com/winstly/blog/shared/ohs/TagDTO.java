package com.winstly.blog.shared.ohs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {

    private String tagCode;
    private String tagName;
    private Long articleCount;
}
