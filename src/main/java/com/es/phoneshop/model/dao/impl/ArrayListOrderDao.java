package com.es.phoneshop.model.dao.impl;

import com.es.phoneshop.model.dao.AbstractDao;
import com.es.phoneshop.model.dao.DaoException;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.dao.OrderDao;
import com.es.phoneshop.model.order.OrderNotFoundExeption;
import com.es.phoneshop.model.product.NotExistIdExeption;

import java.util.ArrayList;

public class ArrayListOrderDao extends AbstractDao<Order> implements OrderDao {
    private ArrayListOrderDao() {
        items = new ArrayList<>();
    }

    @Override
    public Order getOrder(Long id) throws OrderNotFoundExeption {
        Order order= null;
        try {
            order = super.getItem(id);
        } catch (DaoException e) {
            throw new OrderNotFoundExeption();
        }
        return order;
    }

    @Override
    public Order getOrderBySecureId(String secureId) throws OrderNotFoundExeption {
        readLock.lock();
        try {
            return items.stream()
                    .filter(order -> secureId.equals(order.getSecureId()))
                    .findAny()
                    .orElseThrow(OrderNotFoundExeption::new);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void save(Order order) {
        try {
            super.save(order);
        } catch (DaoException e) {
            throw new NotExistIdExeption();
        }
    }


    public static ArrayListOrderDao getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        static final ArrayListOrderDao INSTANCE = new ArrayListOrderDao();
    }
}
