package com.emergency.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class DeptTreeDto {
    private String id;
    
    @JsonProperty("dept_name")
    private String deptName;
    
    private List<DeptTreeDto> children;
}
