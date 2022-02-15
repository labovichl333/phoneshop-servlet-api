package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Currency;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeleteCartItemServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @Mock
    private ServletConfig servletConfig;
    @Mock
    private HttpSession session;


    private DeleteCartItemServlet servlet = new DeleteCartItemServlet();
    private ProductDao productDao = ArrayListProductDao.getInstance();
    private final Currency usd = Currency.getInstance("USD");

    @Before
    public void setup() throws ServletException {
        servlet.init(servletConfig);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/0");
        when(request.getContextPath()).thenReturn("/phoneshop-servlet-api");

        servlet.doPost(request, response);

        verify(response).sendRedirect(request.getContextPath() + "/cart");
    }
}