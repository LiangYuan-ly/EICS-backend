package com.emergency.material.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;

@Data
public class MaterialInoutDto {
    private Integer id;

    @JsonProperty("in_out")
    private Integer inOut; // 1: 入库, 0: 出库

    private Integer num;

    private String remark;

    @JsonProperty("create_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;
}