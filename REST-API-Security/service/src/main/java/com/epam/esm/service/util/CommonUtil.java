package com.epam.esm.service.util;

import com.epam.esm.service.exception.InvalidParametersException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommonUtil {

    public Sort buildSort(List<String> sortColumns, List<String> orderType) {
        Sort sort = Sort.unsorted();
        for (int i = 0; i < sortColumns.size(); i++) {
            if (orderType.iterator().hasNext()) {
                if (orderType.get(i).equals("asc")) {
                    sort.and(Sort.Order.asc(sortColumns.get(i)));
                } else {
                    sort.and(Sort.Order.desc(sortColumns.get(i)));
                }
            } else {
                sort.and(Sort.Order.asc(sortColumns.get(i)));
            }
        }
        return sort;
    }

    public Pageable getPageable(Integer page, Integer size, Sort sort) {
        Pageable pageable;
        try {
            pageable = PageRequest.of(page, size, sort);
        } catch (IllegalArgumentException e) {
            throw new InvalidParametersException("message.pagination.invalid");
        }
        return pageable;
    }
}
