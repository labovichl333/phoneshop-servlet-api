package com.es.phoneshop.web;

import com.es.phoneshop.model.order.OrderNotFoundExeption;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderOverviewPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private ServletConfig servletConfig;
    @Mock
    private RequestDispatcher requestDispatcher;

    private OrderOverviewPageServlet servlet = new OrderOverviewPageServlet();

    @Before
    public void setup() throws ServletException {
        servlet.init(servletConfig);
    }

    @Test(expected = OrderNotFoundExeption.class)
    public void testDoGet() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/randomId");
        servlet.doGet(request, response);
        verify(requestDispatcher).forward(request, response);
    }

}