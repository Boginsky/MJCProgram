package com.epam.esm.model.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PUBLIC)
public class CustomPage<T> {

    private List<T> content;
    private int currentPage;
    private int amountOfPages;
    private int firstPage;
    private int lastPage;
    private int pageSize;

}
