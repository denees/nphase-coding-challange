package com.nphase.entity;

import java.math.BigDecimal;
import java.util.List;

public class ShoppingCart {

    public static final long DISCOUNT_PERCENT_FOR_ITEMS = 10;
    public static final long DISCOUNT_PERCENT_FOR_CATEGORY = 10;
    public static final long ITEMS_BYPASSING_QUANTITY_FOR_DISCOUNT = 3;

    private final List<Product> products;

    public ShoppingCart(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }


}
