package com.emergency.news.controller;

import com.emergency.news.common.PageResult;
import com.emergency.news.common.Result;
import com.emergency.news.dto.NewsDto;
import com.emergency.news.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/admin/news")
    public Result<PageResult<NewsDto>> getNewsList(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String news_name,
            @RequestParam(required = false) String publish_company,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        return Result.success(newsService.getNewsList(pageNum, pageSize, news_name, publish_company, startTime, endTime));
    }

    @GetMapping("/admin/news/{id}")
    public Result<NewsDto> getNewsById(@PathVariable Integer id) {
        return Result.success(newsService.getNewsById(id));
    }

    @PostMapping("/admin/addnews")
    public Result<Void> addNews(@RequestBody NewsDto newsDto) {
        newsService.addNews(newsDto);
        return Result.success("新增成功");
    }

    @PostMapping("/admin/savenews")
    public Result<Void> saveNews(@RequestBody NewsDto newsDto) {
        newsService.saveNews(newsDto);
        return Result.success("保存成功");
    }

    @PutMapping("/admin/updatenews")
    public Result<Void> updateNews(@RequestBody NewsDto newsDto) {
        newsService.updateNews(newsDto);
        return Result.success("修改成功");
    }

    @DeleteMapping("/admin/delnews")
    public Result<Void> deleteNews(@RequestBody Map<String, List<Integer>> payload) {
        List<Integer> ids = payload.get("ids");
        if (ids != null && !ids.isEmpty()) {
            newsService.deleteNews(ids);
        }
        return Result.success("删除成功");
    }

    @PostMapping("/news/upload")
    public Result<String> uploadPhoto(@RequestParam("file") MultipartFile file) {
        String url = newsService.uploadPhoto(file);
        return Result.success(url, "上传成功");
    }
}
