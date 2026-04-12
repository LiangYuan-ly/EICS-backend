package com.emergency.userservice.dto;

import lombok.Data;
import java.util.List;

@Data
public class DeleteIdsReq {
    private List<Integer> ids;
}
