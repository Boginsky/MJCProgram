package com.epam.esm.model.query;

public final class Query {

    private Query() {
    }

    public static final String GET_MOST_WILDLY_USED_TAG_WITH_HIGHEST_COST = "SELECT t.id as 'id', t.name as 'tagName', MAX(o.total_price) as 'highestCost'\n" +
            "FROM orders o\n" +
            "JOIN gift_certificate_tag ct on o.gift_certificate_id = ct.gift_certificate_id\n" +
            "JOIN tag t on ct.tag_id = t.id\n" +
            "WHERE o.user_id = :userId\n" +
            "GROUP BY t.id\n" +
            "ORDER BY COUNT(t.id) DESC, MAX(o.total_price) DESC\n" +
            "LIMIT 1";
}
