package cn.yanshisan.blog.content.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatsVO {

    private long totalArticles;
    private long totalViews;
    private long totalComments;
    private long totalLinks;
}
