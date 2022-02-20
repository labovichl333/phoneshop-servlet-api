package com.es.phoneshop.model.order;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.dao.OrderDao;
import com.es.phoneshop.model.dao.impl.ArrayListOrderDao;
import com.es.phoneshop.model.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.OutOfStockExeption;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.dao.ProductDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

import static junit.framework.TestCase.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultOrderServiceTest {
    private ProductDao productDao;
    private OrderDao orderDao;
    private OrderService orderService;
    private CartService cartService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;

    private Cart cart;

    @Before
    public void setup() throws OutOfStockExeption {
        productDao = ArrayListProductDao.getInstance();
        productDao.save(new Product(null, null, BigDecimal.ONE, null, 1, null));
        orderService = DefaultOrderService.getInstance();
        orderDao = ArrayListOrderDao.getInstance();
        cartService = DefaultCartService.getInstance();
        session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute(any())).thenReturn(null);

        this.cart = cartService.getCart(request);
        cartService.add(cart, 0L, 1);
    }

    @Test
    public void testGetCart() {
        assertNotNull(orderService.getOrder(cart));
    }

    @Test
    public void testPaymentMethods() {
        assertNotNull(orderService.getPaymentMethods());
    }

    @Test
    public void testPlaceOrder() {
        Order order = new Order();
        orderService.placeOrder(cart, order);

        assertNotNull(orderDao.getOrder(order.getId()));
    }
}