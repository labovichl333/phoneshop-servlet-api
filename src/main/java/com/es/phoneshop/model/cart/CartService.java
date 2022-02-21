package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.OutOfStockExeption;

import javax.servlet.http.HttpServletRequest;

public interface CartService {
    Cart getCart(HttpServletRequest request);

    void add(Cart cart, Long productId, int quantity) throws OutOfStockExeption, IllegalArgumentException;

    void update(Cart cart, Long productId, int quantity) throws OutOfStockExeption, IllegalArgumentException;

    void delete(Cart cart, Long productId);

    void cleanCart(Cart cart);
}
