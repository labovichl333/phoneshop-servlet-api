package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.dao.impl.ArrayListOrderDao;
import com.es.phoneshop.model.order.DefaultOrderService;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.dao.OrderDao;
import com.es.phoneshop.model.order.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig servletConfig;
    @Mock
    private HttpSession session;

    @Mock
    private Cart cart;
    @Spy
    private CheckoutPageServlet servlet = new CheckoutPageServlet();
    @Spy
    private OrderService orderService = DefaultOrderService.getInstance();
    @Spy
    private CartService cartService = DefaultCartService.getInstance();
    @Spy
    private Order order = new Order();
    private Locale locale = Locale.getDefault();
    private OrderDao orderDao = ArrayListOrderDao.getInstance();
    private final Currency USD = Currency.getInstance("USD");

    @Before
    public void setup() throws ServletException {
        servlet.init(servletConfig);
        order.setTotalQuantity(1);
        order.setTotalCost(BigDecimal.valueOf(15));
        order.setDeliveryCost(BigDecimal.valueOf(5));
        order.setSubtotal(BigDecimal.valueOf(10));
        orderDao.save(order);

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute(any())).thenReturn(cart);
        when(cart.getTotalCost()).thenReturn(BigDecimal.valueOf(15));
        when(cartService.getCart(request)).thenReturn(cart);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).setAttribute(eq("order"), any());
        verify(request).setAttribute(eq("paymentMethods"), any());
        verify(request).setAttribute(eq("minDate"), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errors"), any());
    }
}