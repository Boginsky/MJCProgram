package com.epam.esm.model.constant;

public final class Query {
    //TAG
    public final static String CREATE_TAG = "INSERT INTO tag(name) VALUES (?)";
    public final static String FIND_TAG_BY_NAME = "SELECT id,name FROM tag WHERE name = ?";
    public final static String FIND_TAG_BY_ID = "SELECT id,name FROM tag WHERE id = ?";
    public final static String GET_ALL_TAGS = "SELECT id,name FROM tag";
    public final static String DELETE_TAG_BY_ID = "DELETE FROM tag WHERE id = ?";
    public final static String DELETE_TAG_BY_NAME = "DELETE FROM tag WHERE name = ?";
    public final static String GET_TAGS_BY_GIFT_CERTIFICATE_ID = "SELECT tag.id,tag.name FROM tag JOIN gift_certificate_has_tag ON tag.id = tag_id JOIN gift_certificate ON gift_certificate_id = gift_certificate.id WHERE gift_certificate.id = ?";
    public final static String UPDATE_TAG_BY_ID = "UPDATE tag SET name = ? WHERE id = ?";

    //GIFT_CERTIFICATE
    public final static String CREATE_GIFT_CERTIFICATE = "INSERT INTO gift_certificate (name, description, price, duration) VALUES(?,?,?,?)";
    public final static String GET_ALL_GIFT_CERTIFICATES = "SELECT id,name,description,price,duration,create_date,last_update_date FROM gift_certificate";
    public final static String DELETE_GIFT_CERTIFICATE_BY_ID = "DELETE FROM gift_certificate WHERE id = ?";
    public final static String DELETE_GIFT_CERTIFICATE_BY_NAME = "DELETE FROM gift_certificate WHERE name = ?";
    public final static String GET_GIFT_CERTIFICATE_BY_ID = "SELECT id,name,description,price,duration,create_date,last_update_date FROM gift_certificate WHERE id = ?";
    public final static String GET_GIFT_CERTIFICATE_BY_NAME = "SELECT id,name,description,price,duration,create_date,last_update_date FROM gift_certificate WHERE name = ?";
    public final static String GET_GIFT_CERTIFICATE_BY_TAG_NAME = "SELECT gift_certificate.id,gift_certificate.name,description,price,duration,create_date,last_update_date FROM gift_certificate JOIN gift_certificate_has_tag ON gift_certificate.id = gift_certificate_id JOIN tag ON tag_id = tag.id WHERE tag.name = ?";
    public final static String CREATE_GIFT_CERTIFICATE_TAG_REFERENCE = "INSERT INTO gift_certificate_has_tag (gift_certificate_id,tag_id)VALUES(?,?)";
    public final static String GET_TAG_IDS_BY_GIFT_CERTIFICATE_ID = "SELECT tag.id FROM tag JOIN gift_certificate_has_tag ON tag.id = tag_id JOIN gift_certificate ON gift_certificate_id = gift_certificate.id WHERE gift_certificate.id = ?";

}
