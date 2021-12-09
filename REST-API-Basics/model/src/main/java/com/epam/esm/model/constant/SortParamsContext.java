package com.epam.esm.model.constant;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SortParamsContext {

    private Map<String, String> sortColumnsWithOrder;

    @JsonCreator
    public SortParamsContext() {
    }

    public SortParamsContext(Map<String, String> sortColumnsWithOrder) {
        this.sortColumnsWithOrder = sortColumnsWithOrder;
    }

    public List<String> getSortColumns() {
        return new ArrayList<>(sortColumnsWithOrder.keySet());
    }

    public List<String> getOrderTypes() {
        return new ArrayList<>(sortColumnsWithOrder.values());
    }
}
