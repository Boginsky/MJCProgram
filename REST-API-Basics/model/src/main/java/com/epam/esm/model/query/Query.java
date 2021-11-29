package com.epam.esm.model.query;

public final class Query {


    //TAG
    public final static String CREATE = "INSERT INTO tags(name) VALUES (?)";
    public final static String FIND_BY_NAME = "SELECT tag_id,name FROM tag WHERE name = ?";
    public final static String FIND_BY_ID = "SELECT tag_id,name FROM tag WHERE tag_id = ?";
    public final static String GET_ALL = "SELECT tag_id,name FROM tag";
    public final static String DELETE_BY_ID = "DELETE FROM tag WHERE tag_id = ?";
    public final static String DELETE_BY_NAME = "DELETE FROM tag WHERE name = ?";
    public final static String GET_TAGS_BY_GIFT_CERTIFICATE_ID = "SELECT tag.id,tag.name FROM tag JOIN gift_certificate_has_tag ON tag.id = tag_id JOIN gift_certificate ON gift_certificate_id = gift_certificate.id WHERE gift_certificate.id = ?";
    public final static String UPDATE_BY_ID = "UPDATE tag SET name = ? WHERE id = ?";

    //GIFT_CERTIFICATE

}
