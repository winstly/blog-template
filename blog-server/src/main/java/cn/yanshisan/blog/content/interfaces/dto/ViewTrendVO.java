package cn.yanshisan.blog.content.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewTrendVO {

    private String date;
    private long views;
}
