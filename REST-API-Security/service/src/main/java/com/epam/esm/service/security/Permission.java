package com.epam.esm.service.security;

public enum Permission {

    TAGS_CREATE("tags:create"),
    TAGS_GET("tags:get"),
    TAGS_DELETE("tags:delete"),
    TAGS_UPDATE("tags:update"),
    BEST_TAG_GET("bestTag:get"),
    GIFT_CERTIFICATES_CREATE("giftCertificate:create"),
    GIFT_CERTIFICATES_GET("giftCertificate:get"),
    GIFT_CERTIFICATES_UPDATE("giftCertificate:update"),
    GIFT_CERTIFICATES_DELETE("giftCertificate:delete"),
    USERS_GET("users:get"),
    ORDERS_GET("orders:get"),
    ORDERS_CREATE("orders:create"),
    SIGN_UP("singUp"),
    LOGIN("login"),
    REFRESH_TOKEN("refresh:token");

    private final String admission;

    Permission(String admission) {
        this.admission = admission;
    }

    public String getAdmission() {
        return admission;
    }
}
