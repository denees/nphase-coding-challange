package com.nphase.service;

import com.nphase.entity.Product;
import com.nphase.entity.ShoppingCart;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.nphase.entity.ShoppingCart.*;

public class ShoppingCartService {

    public BigDecimal calculateTotalPrice(ShoppingCart shoppingCart) {
        return shoppingCart.getProducts()
                .stream()
                .map(product -> product.getPricePerUnit().multiply(BigDecimal.valueOf(product.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    /**
     * We would like to reward the clients that are buying products in bulk.
     * If the client buys more than 3 items of the same product, we are giving him 10% discount for this product.
     *
     * @param shoppingCart
     * @return a BigDecimal of shoppingCart total price with discount for all the items from one category
     */
    public BigDecimal calculateTotalPriceWithDiscount(ShoppingCart shoppingCart) {

        List<BigDecimal> listOfPrices = new ArrayList<>();
        shoppingCart.getProducts()
                .forEach(
                        product -> {
                            BigDecimal totalPrice = BigDecimal.valueOf(product.getPricePerUnit().floatValue() * product.getQuantity());
                            if (product.getQuantity() > ITEMS_BYPASSING_QUANTITY_FOR_DISCOUNT) {
                                listOfPrices.add(totalPrice.subtract(totalPrice.divide(BigDecimal.valueOf(DISCOUNT_PERCENT_FOR_ITEMS))));
                            } else {
                                listOfPrices.add(totalPrice);
                            }
                        }
                );

        return listOfPrices.stream().reduce(BigDecimal::add).get().setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * We would like to introduce the concept of item category and expand our discount policies to the entire category.
     * If the client buys more than 3 items of the product within the same category, we are giving him 10% discount for all product in this category.
     *
     * @param shoppingCart
     * @return a BigDecimal of shoppingCart total price with discount for all the items from one category
     */
    public BigDecimal calculateTotalPriceWithDiscountGroupedByCategory(ShoppingCart shoppingCart) {

        List<BigDecimal> listOfPrices = new ArrayList<>();
        shoppingCart.getProducts()
                .stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.toList()))
                .entrySet()
                .forEach(
                        item -> {
                            List<Product> value = item.getValue();
                            int q = value.stream().map(val -> val.getQuantity()).reduce((x, y) -> x + y).get();

                            value.forEach(product -> {
                                BigDecimal totalPrice = BigDecimal.valueOf(product.getPricePerUnit().floatValue() * product.getQuantity());
                                if (q > ITEMS_BYPASSING_QUANTITY_FOR_DISCOUNT) {
                                    listOfPrices.add(totalPrice.subtract(totalPrice.divide(BigDecimal.valueOf(DISCOUNT_PERCENT_FOR_CATEGORY))));
                                } else {
                                    listOfPrices.add(totalPrice);
                                }
                            });

                        }
                );

        return listOfPrices.stream().reduce(BigDecimal::add).get().setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
