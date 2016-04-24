package com.ytripapp.api.client.feign.support;

import lombok.Getter;

import java.util.List;

@Getter
public class Page<T> {

    int count;
    int totalCount;
    List<T> content;
    PageRequest pageRequest;

    public Page(List<T> content, int totalCount, PageRequest pageRequest) {
        assert content != null;
        this.content = content;
        this.count = content.size();
        this.totalCount = totalCount;
        this.pageRequest = pageRequest;
    }

}
