package com.epam.esm.model.util;

import org.springframework.data.domain.Pageable;

import java.util.List;

public class QueryBuildHelper {

    private static final String BLANK_FOR_UPDATE_GIFT_CERTIFICATE_BY_ID = "UPDATE gift_certificate SET ";
    private static final String BLANK_FOR_GIFT_CERTIFICATE = "SELECT id,name,description,price,duration,create_date,last_update_date FROM gift_certificate";
    private static final String BLANK_FOR_GIFT_CERTIFICATE_WITH_MULTI_TAG_NAMES = "SELECT DISTINCT gift_certificate.id,gift_certificate.name,description, price, duration, create_date, last_update_date FROM gift_certificate JOIN gift_certificate_has_tag ON gift_certificate.id = gift_certificate_id JOIN tag ON tag_id = tag.id";
    private static final String NAME = "name";
    private static final String DURATION = "duration";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";


    public String buildSortingQuery(List<String> sortColumns, List<String> orderTypes, List<String> filterBy) {
        StringBuilder stringBuilder = new StringBuilder(BLANK_FOR_GIFT_CERTIFICATE);
        buildFilteringQuery(stringBuilder, filterBy);
        if (!sortColumns.isEmpty()) {
            stringBuilder.append(" ORDER BY ");
            for (int i = 0; i < sortColumns.size(); i++) {
                if (i != 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(sortColumns.get(i)).append(" ");
                stringBuilder.append(i < orderTypes.size() ? orderTypes.get(i) : "ASC");
            }
        }
        return stringBuilder.toString();
    }

    private String buildFilteringQuery(StringBuilder stringBuilder, List<String> filterBy) {
        if (!filterBy.isEmpty()) {
            stringBuilder.append(" WHERE ");
        }
        for (int i = 0; i < filterBy.size(); i++) {
            if (i != 0) {
                stringBuilder.append(" AND ");
            }
            if (i == 0) {
                stringBuilder.append(NAME);
            } else if (i == 1) {
                stringBuilder.append(DESCRIPTION);
            }
            stringBuilder.append(" LIKE \'%").append(filterBy.get(i)).append("%\'");
        }
        return stringBuilder.toString();
    }

    public String buildQueryWithMultiTagNames(List<String> tagNameList) {
        StringBuilder stringBuilder = new StringBuilder(BLANK_FOR_GIFT_CERTIFICATE_WITH_MULTI_TAG_NAMES);
        if (!tagNameList.isEmpty()) {
            stringBuilder.append(" WHERE ");
        }
        for (int i = 0; i < tagNameList.size(); i++) {
            if (i != 0) {
                stringBuilder.append(" AND ");
            }
            stringBuilder.append("tag.name LIKE \'%");
            stringBuilder.append(tagNameList.get(i));
            stringBuilder.append("%\'");
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

    public String buildPaginationQuery(String query, Pageable pageable) {
        StringBuilder stringBuilder = new StringBuilder(query);
        stringBuilder.append(" LIMIT ").append(pageable.getPageSize()).append(" ").append("OFFSET ").append(pageable.getOffset());
        return stringBuilder.toString();
    }
}
