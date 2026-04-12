package com.emergency.news.service;

import com.emergency.news.common.PageResult;
import com.emergency.news.dto.NewsDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NewsService {
    PageResult<NewsDto> getNewsList(Integer pageNum, Integer pageSize, String newsName, String publishCompany, String startTime, String endTime);
    NewsDto getNewsById(Integer id);
    void addNews(NewsDto newsDto);
    void updateNews(NewsDto newsDto);
    void saveNews(NewsDto newsDto); // save is like add/update but sets status to 1 (draft)
    void deleteNews(List<Integer> ids);
    String uploadPhoto(MultipartFile file);
}
