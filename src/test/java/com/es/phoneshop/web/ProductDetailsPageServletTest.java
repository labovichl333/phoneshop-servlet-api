package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
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
public class ProductDetailsPageServletTest {
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
    @Spy
    private ProductDetailsPageServlet servlet = new ProductDetailsPageServlet();
    private ProductDao productDao = ArrayListProductDao.getInstance();
    private final Currency usd = Currency.getInstance("USD");

    @Before
    public void setup() throws ServletException {
        servlet.init(servletConfig);
        Product product = new Product("for  test ", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "Samsung.jpg");
        productDao.save(product);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getLocale()).thenReturn(locale);
        when(request.getContextPath()).thenReturn("/phoneshop-servlet-api");
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/0");

        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("product"), any());
    }

    @Test
    public void TestDoPostWithCorrectQuantity() throws ServletException, IOException {
        Long productId = 0L;
        when(request.getPathInfo()).thenReturn("/0");
        when(request.getParameter("quantity")).thenReturn("2");

        servlet.doPost(request, response);

        verify(response).sendRedirect(request.getContextPath() + "/products/" + productId + "?message=Product added to cart");

    }

    @Test
    public void TestDoPostWithNegativeQuantity() throws ServletException, IOException {
        Long productId = 0L;
        when(request.getPathInfo()).thenReturn("/0");
        when(request.getParameter("quantity")).thenReturn("-2");

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Quantity must be positive");
        verify(servlet).doGet(request, response);

    }

    @Test
    public void TestDoPostWithNotANumberQuantity() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/0");
        when(request.getParameter("quantity")).thenReturn("dvzdfv");

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Not a number");
        verify(servlet).doGet(request, response);

    }

    @Test
    public void TestDoPostWithNotOutOfStockQuantity() throws ServletException, IOException {
        Long productId = 0L;
        when(request.getPathInfo()).thenReturn("/0");
        when(request.getParameter("quantity")).thenReturn("10000");

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Out of stock, avalible "
                + productDao.getProduct(productId).getStock());
        verify(servlet).doGet(request, response);

    }
}