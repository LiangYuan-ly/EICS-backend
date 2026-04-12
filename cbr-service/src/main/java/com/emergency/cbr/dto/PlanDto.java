package com.emergency.cbr.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;

@Data
public class PlanDto {
    private Integer id;
    
    @JsonProperty("plan_code")
    private String planCode;
    
    @JsonProperty("plan_title")
    private String planTitle;
    
    @JsonProperty("plan_type")
    private Integer planType;
    
    @JsonProperty("category_code")
    private String categoryCode;
    
    @JsonProperty("category_name")
    private String categoryName;
    
    @JsonProperty("plan_level")
    private Integer planLevel;
    
    @JsonProperty("publish_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private String publishTime; // changed to String to handle partial dates from frontend or format automatically
    
    @JsonProperty("dept_id")
    private Integer deptId;
    
    @JsonProperty("dept_code")
    private String deptCode;
    
    @JsonProperty("dept_name")
    private String deptName;
    
    private Integer status;
    
    @JsonProperty("plan_url")
    private String planUrl;
    
    @JsonProperty("create_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;
    
    @JsonProperty("update_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updateTime;

    @JsonProperty("attachment_id")
    private Integer attachmentId;

    @JsonProperty("attachment_name")
    private String attachmentName;

    @JsonProperty("attachment_url")
    private String attachmentUrl;
}
