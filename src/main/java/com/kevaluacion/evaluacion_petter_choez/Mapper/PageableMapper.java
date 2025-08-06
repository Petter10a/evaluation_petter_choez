package com.kevaluacion.evaluacion_petter_choez.Mapper;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.kevaluacion.evaluacion_petter_choez.Constant.PaginatorConst;

@Component
public class PageableMapper {

    /**
     * Converts pagination parameters to a Pageable object.
     *
     * @param page          the page number (0-based)
     * @param size          the size of the page
     * @param sortBy        the field to sort by
     * @param sortDirection the direction of sorting (ASC or DESC)
     * @return a Pageable object with the specified parameters
     */
    public Pageable toPageable(int page, int size, String sortBy, String sortDirection) {
        page = page < 0 ? PaginatorConst.DEFAULT_PAGE : page;
        size = size <= 0 ? PaginatorConst.DEFAULT_SIZE : size;
        size = size > PaginatorConst.MAX_SIZE ? PaginatorConst.MAX_SIZE : size;
        sortBy = (sortBy == null || sortBy.isEmpty()) ? PaginatorConst.DEFAULT_SORT_BY : sortBy;
        sortDirection = (sortDirection == null || sortDirection.isEmpty()) ? PaginatorConst.DEFAULT_SORT_DIRECTION
                : sortDirection;
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        Sort sort = Sort.by(direction, sortBy);
        return PageRequest.of(page, size, sort);
    }

}
