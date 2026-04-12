package com.emergency.news.service.impl;

import com.emergency.news.common.PageResult;
import com.emergency.news.dto.NewsDto;
import com.emergency.news.entity.Admin;
import com.emergency.news.entity.News;
import com.emergency.news.repository.AdminRepository;
import com.emergency.news.repository.NewsRepository;
import com.emergency.news.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;
import java.io.File;
import java.io.IOException;

@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private AdminRepository adminRepository;

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public PageResult<NewsDto> getNewsList(Integer pageNum, Integer pageSize, String newsName, String publishCompany,
            String startTime, String endTime) {
        PageRequest pageRequest = PageRequest.of(pageNum > 0 ? pageNum - 1 : 0, pageSize,
                Sort.by(Sort.Direction.DESC, "createTime"));

        Specification<News> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("deleted"), 0));

            if (StringUtils.hasText(newsName)) {
                predicates.add(cb.like(root.get("newsName"), "%" + newsName + "%"));
            }
            if (StringUtils.hasText(publishCompany)) {
                predicates.add(cb.like(root.get("publishCompany"), "%" + publishCompany + "%"));
            }
            if (StringUtils.hasText(startTime)) {
                LocalDateTime start = startTime.contains("T") ? OffsetDateTime.parse(startTime).toLocalDateTime()
                        : LocalDate.parse(startTime, dateFormatter).atStartOfDay();
                predicates.add(cb.greaterThanOrEqualTo(root.get("publishTime"), start));
            }
            if (StringUtils.hasText(endTime)) {
                LocalDateTime end = endTime.contains("T") ? OffsetDateTime.parse(endTime).toLocalDateTime()
                        : LocalDate.parse(endTime, dateFormatter).atTime(23, 59, 59);
                predicates.add(cb.lessThanOrEqualTo(root.get("publishTime"), end));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<News> page = newsRepository.findAll(spec, pageRequest);

        List<NewsDto> dtos = page.getContent().stream().map(this::convertToDto).collect(Collectors.toList());
        return new PageResult<>(dtos, page.getTotalElements());
    }

    @Override
    public NewsDto getNewsById(Integer id) {
        News news = newsRepository.findById(id).orElseThrow(() -> new RuntimeException("News not found"));
        return convertToDto(news);
    }

    @Override
    public void addNews(NewsDto dto) {
        News news = new News();
        updateEntityFromDto(news, dto);
        newsRepository.save(news);
    }

    @Override
    public void updateNews(NewsDto dto) {
        if (dto.getId() == null || dto.getId() == 0) {
            throw new RuntimeException("ID is missing");
        }
        News news = newsRepository.findById(dto.getId()).orElseThrow(() -> new RuntimeException("News not found"));
        updateEntityFromDto(news, dto);
        newsRepository.save(news);
    }

    @Override
    public void saveNews(NewsDto dto) {
        News news;
        if (dto.getId() != null && dto.getId() > 0) {
            news = newsRepository.findById(dto.getId()).orElse(new News());
        } else {
            news = new News();
        }
        updateEntityFromDto(news, dto);
        news.setNewsStatus(1); // 1 = draft / waiting
        newsRepository.save(news);
    }

    @Override
    public void deleteNews(List<Integer> ids) {
        List<News> newsList = newsRepository.findAllById(ids);
        for (News news : newsList) {
            news.setDeleted(1);
        }
        newsRepository.saveAll(newsList);
    }

    @Override
    public String uploadPhoto(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }
        // Simplified local mock upload
        try {
            String dirPath = System.getProperty("user.dir") + "/uploads/";
            File dir = new File(dirPath);
            if (!dir.exists())
                dir.mkdirs();

            String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            File dest = new File(dirPath + filename);
            file.transferTo(dest);
            return "http://localhost:8080/uploads/" + filename; // Return mock URL
        } catch (IOException e) {
            throw new RuntimeException("File upload failed", e);
        }
    }

    private NewsDto convertToDto(News news) {
        NewsDto dto = new NewsDto();
        dto.setId(news.getId());
        dto.setNews_name(news.getNewsName());
        dto.setPublish_company(news.getPublishCompany());
        if (news.getPublishTime() != null) {
            dto.setPublish_time(news.getPublishTime().format(dateFormatter));
        }
        dto.setNews_photo(news.getNewsPhoto());
        dto.setNews_url(news.getNewsUrl());
        dto.setNews_status(news.getNewsStatus());
        if (news.getCreateTime() != null) {
            dto.setCreate_time(news.getCreateTime().format(dateTimeFormatter));
        }
        if (news.getUpdateTime() != null) {
            dto.setUpdate_time(news.getUpdateTime().format(dateTimeFormatter));
        }
        if (news.getAdminId() != null) {
            adminRepository.findById(news.getAdminId()).ifPresent(admin -> dto.setAdmin_name(admin.getAdminName()));
        }
        return dto;
    }

    private void updateEntityFromDto(News news, NewsDto dto) {
        news.setNewsName(dto.getNews_name());
        news.setPublishCompany(dto.getPublish_company());
        if (StringUtils.hasText(dto.getPublish_time())) {
            news.setPublishTime(LocalDate.parse(dto.getPublish_time(), dateFormatter).atStartOfDay());
        }
        news.setNewsPhoto(dto.getNews_photo());
        news.setNewsUrl(dto.getNews_url());
        if (dto.getNews_status() != null) {
            news.setNewsStatus(dto.getNews_status());
        }
        if (StringUtils.hasText(dto.getAdmin_name())) {
            adminRepository.findByAdminName(dto.getAdmin_name()).ifPresent(admin -> news.setAdminId(admin.getId()));
        }
    }
}
