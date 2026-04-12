package com.emergency.eventservice.dto;

import lombok.Data;

@Data
public class AttachmentDto {
    private Integer id;
    private String name;
    private String url;
    private String type;
}
