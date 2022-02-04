package com.es.phoneshop.model.product.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.OutOfStockExeption;
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
public class DefaultCartServiceTest {
    @Mock
    private HttpSession session;
    @Mock
    private HttpServletRequest request;

    private final Currency usd = Currency.getInstance("USD");
    private Product correctProductWithStoch10 = new Product("for test", "Samsung Galaxy S",
            new BigDecimal(100), usd, 10, "20S.jpg");

    private ProductDao productDao;
    private CartService cartService;
    private Cart cart;

    @Before
    public void setup() {
        cartService = DefaultCartService.getInstance();
        productDao = ArrayListProductDao.getInstance();
        productDao.save(correctProductWithStoch10);
        when(request.getSession()).thenReturn(session);
        cart = cartService.getCart(request);
    }

    @Test
    public void correctAddingProductToCart() throws OutOfStockExeption {
        cartService.add(cart, correctProductWithStoch10.getId(), 1);
        cartService.add(cart, correctProductWithStoch10.getId(), 1);

        assertTrue(cart.getItems().size() == 1);
    }

    @Test(expected = OutOfStockExeption.class)
    public void uncorrectStockAddingProductToCart() throws OutOfStockExeption {
        cartService.add(cart, correctProductWithStoch10.getId(), 1000);

        assertTrue(cart.getItems().size() == 1);
    }

}
