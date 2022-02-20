package com.es.phoneshop.model.dao.impl;

import com.es.phoneshop.model.dao.OrderDao;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderNotFoundExeption;
import com.es.phoneshop.model.product.NotExistIdExeption;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArrayListOrderDaoTest {
    private OrderDao orderDao;

    @Before
    public void setup() throws OrderNotFoundExeption {
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Test
    public void testGetOrderBySecureId() {
        Order orderToSave = new Order();
        orderToSave.setSecureId("qwert");
        orderDao.save(orderToSave);
        Order result = orderDao.getOrderBySecureId(orderToSave.getSecureId());

        assertEquals(result, orderToSave);
    }

    @Test
    public void testGetOrder() {
        Order orderToSave = new Order();
        orderDao.save(orderToSave);
        Order result = orderDao.getOrder(orderToSave.getId());

        assertEquals(result, orderToSave);
    }


    @Test
    public void testSaveOrder() {
        Order orderToSave = new Order();

        orderDao.save(orderToSave);
        Order result = orderDao.getOrder(orderToSave.getId());

        assertEquals(result, orderToSave);
    }

    @Test(expected = NotExistIdExeption.class)
    public void testSaveOrderWithUncorrectId() {
        Order orderToSave = new Order();
        orderToSave.setId(1000L);

        orderDao.save(orderToSave);
    }

}
