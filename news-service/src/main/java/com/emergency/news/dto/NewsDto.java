package com.emergency.news.dto;

import lombok.Data;

@Data
public class NewsDto {
    private Integer id;
    private String news_name;
    private String publish_time; // mapped to LocalDateTime or parsed as Date
    private String publish_company;
    private String news_photo;
    private String news_url;
    private Integer news_status;
    private String admin_name;
    private String create_time;
    private String update_time;
}
