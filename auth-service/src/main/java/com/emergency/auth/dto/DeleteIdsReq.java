package com.emergency.auth.dto;

import lombok.Data;
import java.util.List;

@Data
public class DeleteIdsReq {
    private List<Integer> ids;
}
