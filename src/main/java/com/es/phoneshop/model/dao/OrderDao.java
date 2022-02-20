package com.es.phoneshop.model.dao;

import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderNotFoundExeption;

public interface OrderDao  {
    Order getOrder(Long id) throws OrderNotFoundExeption;

    void save(Order order);

    Order getOrderBySecureId(String secureId) throws OrderNotFoundExeption;
}
