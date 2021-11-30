package com.epam.esm.model.util;

import java.util.List;

public class QueryBuildHelper {

    private static final String BLANK_FOR_GIFT_CERTIFICATE = "SELECT id,name,description,price,duration,create_date,last_update_date FROM gift_certificate ";

    public String buildSortingQuery(List<String> sortColumns, List<String> orderTypes) {
        StringBuilder stringBuilder = new StringBuilder(BLANK_FOR_GIFT_CERTIFICATE).append("ORDER BY ");
        for (int i = 0; i < sortColumns.size(); i++) {
            if (i != 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(sortColumns.get(i)).append(" ");
            stringBuilder.append(i < orderTypes.size() ? orderTypes.get(i) : "ASC");
        }
        return stringBuilder.toString();
    }

    public String buildFilteringQuery(String name, String description){
        StringBuilder stringBuilder = new StringBuilder(BLANK_FOR_GIFT_CERTIFICATE);
        stringBuilder.append("WHERE name ");
        stringBuilder.append("LIKE \'%").append(name).append("%\' AND ");
        stringBuilder.append("description ");
        stringBuilder.append("LIKE \'%").append(description).append("%\'");
        return stringBuilder.toString();
    }
}
