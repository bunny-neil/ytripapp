package com.ytripapp.api.client.feign.decoder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.domain.Sort;

@Getter
@EqualsAndHashCode
public class PageRequest {

    public static final PageRequest DEFAULT = new PageRequest(0, 20);

    int offset;
    int size;
    Sort sort;

    public PageRequest(int offset, int size) {
        this.offset = offset;
        this.size = size;
    }

    @JsonCreator
    public PageRequest(@JsonProperty("offset") int offset,
                       @JsonProperty("size") int size,
                       @JsonProperty("sort") Sort sort) {
        this.offset = offset;
        this.size = size;
        this.sort = sort;
    }

}
