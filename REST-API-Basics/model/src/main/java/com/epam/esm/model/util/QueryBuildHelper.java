package com.epam.esm.model.util;

import java.util.List;

public class QueryBuildHelper {

    private static final String BLANK_FOR_GIFT_CERTIFICATE = "SELECT id,name,description,price,duration,create_date,last_update_date FROM gift_certificate ";
    public final static String BLANK_FOR_UPDATE_GIFT_CERTIFICATE_BY_ID = "UPDATE gift_certificate SET ";
    private static final String NAME = "name";
    private static final String DURATION = "duration";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";


    public String buildSortingQuery(List<String> sortColumns, List<String> orderTypes) {
        StringBuilder stringBuilder = new StringBuilder(BLANK_FOR_GIFT_CERTIFICATE);
        if (!sortColumns.isEmpty()) {
            stringBuilder.append("ORDER BY ");
        }
        for (int i = 0; i < sortColumns.size(); i++) {
            if (i != 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(sortColumns.get(i)).append(" ");
            stringBuilder.append(i < orderTypes.size() ? orderTypes.get(i) : "ASC");
        }
        return stringBuilder.toString();
    }

    public String buildFilteringQuery(String name, String description) {
        StringBuilder stringBuilder = new StringBuilder(BLANK_FOR_GIFT_CERTIFICATE);
        if (name != null) {
            stringBuilder.append("WHERE name ");
            stringBuilder.append("LIKE \'%").append(name).append("%\'");
        }
        if (description != null) {
            stringBuilder.append(" AND description ").append("");
            stringBuilder.append("LIKE \'%").append(description).append("%\'");
        }
        return stringBuilder.toString();
    }

    public String buildQueryForUpdate(List<String> columns) {
        StringBuilder stringBuilder = new StringBuilder(BLANK_FOR_UPDATE_GIFT_CERTIFICATE_BY_ID);
        if (columns != null) {
            for (String column : columns) {
                switch (column) {
                    case NAME:
                    case PRICE:
                    case DESCRIPTION:
                    case DURATION:
                        stringBuilder.append(column);
                        stringBuilder.append("=?,");
                        break;
                }
            }
        }
        return stringBuilder.append("last_update_date = NOW() WHERE id = ?").toString();
    }
}
