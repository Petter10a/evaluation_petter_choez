package com.kevaluacion.evaluacion_petter_choez.Util;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Getter;
import lombok.Setter;

/**
 * Paginated response DTO for API responses.
 *
 * @param <T> the type of data contained in the response
 */
@Getter
@Setter
public class PaginatedResponse<T> {
    private List<T> content;
    private int page;
    private int size;
    private long total;
    private int pages;

    public PaginatedResponse(Page<T> page) {
        this.content = page.getContent();
        this.page = page.getNumber();
        this.size = page.getSize();
        this.total = page.getTotalElements();
        this.pages = page.getTotalPages();
    }

    public static <T> PaginatedResponse<T> of(Page<T> page) {
        return new PaginatedResponse<>(page);
    }
}
