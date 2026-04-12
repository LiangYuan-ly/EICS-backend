package com.emergency.userservice.common;

import lombok.Data;
import java.util.List;

@Data
public class PageData<T> {
    private List<T> list;
    private Long total;

    public PageData() {}

    public PageData(List<T> list, Long total) {
        this.list = list;
        this.total = total;
    }
}
