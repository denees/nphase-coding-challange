package com.nphase.service;


import com.nphase.entity.Product;
import com.nphase.entity.ShoppingCart;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.naming.OperationNotSupportedException;
import java.math.BigDecimal;
import java.util.Arrays;

public class ShoppingCartServiceTest {
    private final ShoppingCartService service = new ShoppingCartService();

    @Test
    public void calculatesPrice()  {
        ShoppingCart cart = new ShoppingCart(Arrays.asList(
                new Product("Tea", BigDecimal.valueOf(5.0), 2),
                new Product("Coffee", BigDecimal.valueOf(6.5), 1)
        ));

        BigDecimal result = service.calculateTotalPrice(cart);

        Assertions.assertEquals(result, BigDecimal.valueOf(16.5));
    }

    /**
     * Given 2 products:
     *  -> name: tea, pricePerUnit: 5, quantity: 5
     *  -> name: coffee, pricePerUnit: 3.5, quantity: 3
     *
     * Expected total is: 22.5 (for tea) + 10.5 (for coffee) = 33.0
     */
    @Test
    public void calculatePriceWithDiscount() {
        ShoppingCart cart = new ShoppingCart(Arrays.asList(
                new Product("Tea", BigDecimal.valueOf(5.0), 5),
                new Product("Coffee", BigDecimal.valueOf(3.5), 3)
        ));

        BigDecimal result = service.calculateTotalPriceWithDiscount(cart);
        Assertions.assertEquals(result.stripTrailingZeros(), (BigDecimal.valueOf(33.0).stripTrailingZeros()));
    }

    /**
     * Given 3 products:
     *  -> name: tea, pricePerUnit: 5.3, quantity: 2, category: drinks
     *  -> name: coffee, pricePerUnit: 3.5, quantity: 2, category: drinks
     *  -> name: cheese, pricePerUnit: 8, quantity: 2, category: food
     *
     * Expected total is: 9.54 (for tea) + 6.30 (for coffee) + 16 (for cheese) = 31.84
     */
    @Test
    public void calculatePriceWithDiscountGroupedByCategory() {
        ShoppingCart cart = new ShoppingCart(Arrays.asList(
                new Product("Tea", BigDecimal.valueOf(5.3), 2, "Drinks"),
                new Product("Coffee", BigDecimal.valueOf(3.5), 2, "Drinks"),
                new Product("Cheese", BigDecimal.valueOf(8), 2, "Food")
        ));

        BigDecimal result = service.calculateTotalPriceWithDiscountGroupedByCategory(cart);
        Assertions.assertEquals(result.stripTrailingZeros(), (BigDecimal.valueOf(31.84).stripTrailingZeros()));
    }
}