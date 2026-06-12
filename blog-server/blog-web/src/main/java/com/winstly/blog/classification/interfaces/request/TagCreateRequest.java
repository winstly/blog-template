package com.winstly.blog.classification.interfaces.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TagCreateRequest {

    @NotBlank
    private String tagCode;

    @NotBlank
    private String tagName;

    private String description;

    private String parentTagCode;
}
