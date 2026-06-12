package com.winstly.blog.diary.domain.entity;

import com.winstly.blog.shared.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@TableName("blog_diary")
public class DiaryDO extends BaseEntity {

    private String diaryCode;
    private String content;
    private String images;     // JSON array of image URLs
    private LocalDate diaryDate;
    private Integer year;      // denormalized for fast filtering
}
