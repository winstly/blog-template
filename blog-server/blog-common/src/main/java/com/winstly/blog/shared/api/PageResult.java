package com.winstly.blog.shared.api;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {

    private List<T> list;
    private Pagination pagination;

    public PageResult(List<T> list, long total, int page, int pageSize) {
        this.list = list;
        this.pagination = new Pagination(page, pageSize, total);
    }

    @Data
    public static class Pagination {
        private int page;
        private int pageSize;
        private long total;

        public Pagination(int page, int pageSize, long total) {
            this.page = page;
            this.pageSize = pageSize;
            this.total = total;
        }
    }
}
