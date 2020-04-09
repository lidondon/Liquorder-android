package com.infolai.liquorder.api;

public final class Api {
    public static final class Authorization {
        public static final String AUTH_HEADER = "Authorization";
        public static final String BEARER_PREFIX = "Bearer ";
    }

    public final class Status {
        public static final String UNAUTHORIZED = "Unauthorized";
    }

    public final class Url {
        public static final String AUTH_BASE_URL = "http://ec2-3-112-213-86.ap-northeast-1.compute.amazonaws.com:8083";
        public static final String LOGIN_URL = "/api/v1/identity/login";
        public static final String REFRESH_TOKEN_URL = "/api/v1/identity/refresh";

        public static final String BASE_URL = "http://ec2-3-112-213-86.ap-northeast-1.compute.amazonaws.com:8086";
        public static final String GET_CELLARERS = "/api/v1/retailer/coopcellarers";
        public static final String GET_CELLARER_ACTIVE_MENU = "/api/v1/retailer/coopcellarer/{id}/activemenu";
        public static final String GET_MENU_CATEGORIES = "/api/v1/retailer/activemenu/{menuId}/categories";
        public static final String GET_MENU_CATEGORY_ITEMS = "/api/v1/retailer/activemenu/{menuId}/items";
        public static final String CREATE_ORDER = "/api/v1/retailer/order/create";
        public static final String UPDATE_ORDER = "/api/v1/retailer/order/{id}/update";
        public static final String GET_ORDERS = "/api/v1/retailer/orders";
        public static final String GET_ORDER_ITEMS = "/api/v1/retailer/order/{orderId}/items";
        public static final String GET_LIQUOR_CATEGORIES = "/api/v1/retailer/liquor/categories";
        public static final String GET_LIQUOR_CATEGORY_ITEMS = "/api/v1/retailer/liquors";
        public static final String GET_FAVORITES = "/api/v1/retailer/favorites";
        public static final String ADD_FAVORITE = "/api/v1/retailer/favorite/add";
        public static final String REMOVE_FAVORITE = "/api/v1/retailer/favorite/remove";
    }
}
