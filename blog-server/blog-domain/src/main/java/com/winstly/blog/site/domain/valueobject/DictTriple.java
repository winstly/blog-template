package com.winstly.blog.site.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictTriple {

    private String bizCode;
    private String subCode;
    private String itemCode;
}
