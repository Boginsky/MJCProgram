package com.epam.esm.model.constant;

public final class Query {
    //TAG
    public static final String CREATE_TAG = "INSERT INTO tag(name) VALUES (?)";
    public static final String GET_TAG_BY_NAME = "SELECT id,name FROM tag WHERE name = ?";
    public static final String GET_TAG_BY_ID = "SELECT id,name FROM tag WHERE id = ?";
    public static final String DELETE_TAG_BY_ID = "DELETE FROM tag WHERE id = ?";
    public static final String GET_TAGS_BY_GIFT_CERTIFICATE_ID = "SELECT tag.id,tag.name FROM tag JOIN gift_certificate_has_tag ON tag.id = tag_id JOIN gift_certificate ON gift_certificate_id = gift_certificate.id WHERE gift_certificate.id = ?";
    public static final String UPDATE_TAG_BY_ID = "UPDATE tag SET name = ? WHERE id = ?";
    public static final String GET_COUNT_OF_ALL_TAGS = "SELECT COUNT(*) FROM tag";
    public static final String GET_ALL_TAGS = "SELECT id,name FROM tag";

    //GIFT_CERTIFICATE
    public static final String CREATE_GIFT_CERTIFICATE = "INSERT INTO gift_certificate (name, description, price, duration) VALUES(?,?,?,?)";
    public static final String GET_ALL_GIFT_CERTIFICATES = "SELECT id,name,description,price,duration,create_date,last_update_date FROM gift_certificate";
    public static final String DELETE_GIFT_CERTIFICATE_BY_ID = "DELETE FROM gift_certificate WHERE id = ?";
    public static final String GET_GIFT_CERTIFICATE_BY_ID = "SELECT id,name,description,price,duration,create_date,last_update_date FROM gift_certificate WHERE id = ?";
    public static final String GET_GIFT_CERTIFICATE_BY_NAME = "SELECT id,name,description,price,duration,create_date,last_update_date FROM gift_certificate WHERE name = ?";
    public static final String CREATE_GIFT_CERTIFICATE_TAG_REFERENCE = "INSERT INTO gift_certificate_has_tag (gift_certificate_id,tag_id)VALUES(?,?)";
    public static final String GET_TAG_IDS_BY_GIFT_CERTIFICATE_ID = "SELECT tag.id FROM tag JOIN gift_certificate_has_tag ON tag.id = tag_id JOIN gift_certificate ON gift_certificate_id = gift_certificate.id WHERE gift_certificate.id = ?";
    public static final String GET_COUNT_OF_ALL_GIFT_CERTIFICATES = "SELECT COUNT(*) FROM gift_certificate";
    public static final String GET_GIFT_CERTIFICATES_BY_USER_ID = "SELECT gift_certificate.id,gift_certificate.name,description, price, duration, create_date, last_update_date FROM gift_certificate JOIN orders ON gift_certificate.id = gift_certificate_id JOIN user ON user_id = user.id WHERE user.id = ?";

    //USER
    public static final String GET_BY_ID = "SELECT id, first_name,last_name FROM user WHERE id = ?";
    public static final String GET_COUNT_OF_ALL_USERS = "SELECT COUNT(*) FROM user";
    public static final String GET_ALL_USERS = "SELECT id, first_name,last_name FROM user";

    //ORDER
    public static final String CREATE_ORDER = "INSERT INTO orders (total_price,gift_certificate_id,user_id) VALUES(?,?,?)";
    public static final String GET_ALL_ORDERS = "SELECT user.id,first_name,last_name,orders.id,date_of_purchase,total_price,gift_certificate_id,name,description,price,duration,create_date,last_update_date FROM orders JOIN user ON user_id = user.id JOIN gift_certificate ON gift_certificate_id = gift_certificate.id";
    public static final String GET_ALL_ORDERS_BY_USER_ID = "SELECT user.id,first_name,last_name,orders.id,date_of_purchase,total_price,gift_certificate_id,name,description,price,duration,create_date,last_update_date FROM orders JOIN user ON user_id = user.id JOIN gift_certificate ON gift_certificate_id = gift_certificate.id WHERE user_id = ?";
    public static final String GET_ALL_ORDERS_BY_ID = "SELECT user.id,first_name,last_name,orders.id,date_of_purchase,total_price,gift_certificate_id,name,description,price,duration,create_date,last_update_date FROM orders JOIN user ON user_id = user.id JOIN gift_certificate ON gift_certificate_id = gift_certificate.id WHERE orders.id = ?";
    public static final String GET_COUNT_OF_ALL_ORDERS = "SELECT COUNT(*) FROM orders";
    public static final String GET_ALL_ORDER_IDS = "SELECT gift_certificate_id,user_id FROM orders";

    //QUERY FOR TASK
    public static final String GET_MOST_WILDLY_USED_TAG_WITH_HIGHEST_COST = "SELECT tag.id, tag.name, MAX(orders.total_price) as 'highestCost' FROM orders JOIN gift_certificate ON orders.gift_certificate_id = gift_certificate.id JOIN gift_certificate_has_tag ON gift_certificate.id = gift_certificate_has_tag.gift_certificate_id JOIN tag ON tag_id = tag.id GROUP BY tag.id ORDER BY COUNT(tag.id) DESC, MAX(orders.total_price) DESC LIMIT 1";


}
