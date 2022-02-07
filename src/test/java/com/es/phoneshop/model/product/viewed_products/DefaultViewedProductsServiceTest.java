package com.es.phoneshop.model.product.viewed_products;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Currency;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class DefaultViewedProductsServiceTest {
    @Mock
    private HttpSession session;
    @Mock
    private HttpServletRequest request;

    private final Currency usd = Currency.getInstance("USD");
    private Product product1 = new Product("for test", "Samsung Galaxy S",
            new BigDecimal(100), usd, 10, "20S.jpg");
    private Product product2 = new Product("for test", "Samsung S",
            new BigDecimal(100), usd, 100, "200.jpg");
    private Product product3 = new Product("for test", "Samsung",
            new BigDecimal(100), usd, 10, "20S.jpg");
    private Product product4 = new Product("for test", "S",
            new BigDecimal(100), usd, 100, "200.jpg");

    private ProductDao productDao;
    private ViewedProductsHolder viewedProductsHolder;
    private ViewedProductsService viewedProductsService;

    @Before
    public void setup() {
        viewedProductsService = DefaultViewedProductsService.getInstance();
        productDao = ArrayListProductDao.getInstance();
        productDao.save(product1);
        productDao.save(product2);
        productDao.save(product3);
        productDao.save(product4);
        when(request.getSession()).thenReturn(session);
        viewedProductsHolder = viewedProductsService.getViewedProductsHolder(request);
    }

    @Test
    public void correctAddindToviewedProducts() {
        viewedProductsService.addViewedProduct(viewedProductsHolder, product1.getId());

        assertTrue(viewedProductsHolder.getProducts().size() == 1);
    }

    @Test
    public void AddindToviewedProductsMoreThanItCanContain() {
        viewedProductsService.addViewedProduct(viewedProductsHolder, product1.getId());
        viewedProductsService.addViewedProduct(viewedProductsHolder, product2.getId());
        viewedProductsService.addViewedProduct(viewedProductsHolder, product1.getId());
        viewedProductsService.addViewedProduct(viewedProductsHolder, product3.getId());
        viewedProductsService.addViewedProduct(viewedProductsHolder, product4.getId());

        assertTrue(viewedProductsHolder.getProducts().size() == 3);
    }

}
