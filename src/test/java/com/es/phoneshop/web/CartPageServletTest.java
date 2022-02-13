package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartPageServletTest {
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

    private Locale locale = Locale.getDefault();


    private CartPageServlet servlet = new CartPageServlet();
    private ProductDao productDao = ArrayListProductDao.getInstance();

    @Before
    public void setup() throws ServletException {
        servlet.init(servletConfig);
        for (int i = 0; i < 4; i++) {
            productDao.save(new Product());
        }
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getLocale()).thenReturn(locale);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        String[] quantityVal = {"fbg", "5", "-7", "1000"};
        String[] productIdVal = {"0", "1", "2", "3"};
        when(request.getParameterValues("quantity")).thenReturn(quantityVal);
        when(request.getParameterValues("productId")).thenReturn(productIdVal);

        servlet.doPost(request, response);

        verify(request).getParameterValues("quantity");
        verify(request).getParameterValues("productId");
    }
}