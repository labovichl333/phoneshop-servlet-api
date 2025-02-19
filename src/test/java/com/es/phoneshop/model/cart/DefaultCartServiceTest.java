package com.es.phoneshop.model.cart;

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
import java.util.Currency;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

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

    @Test
    public void deleteProductTest() throws OutOfStockExeption {
        int quantity = 5;
        Long productId = 0L;
        cartService.add(cart, productId, quantity);

        cartService.delete(cart, productId);

        assertTrue(quantity == cart.getTotalQuantity() + quantity);

    }

    @Test
    public void correctUpdateTest() throws OutOfStockExeption {
        Product productWithStock100AndId1 = new Product(null, null, new BigDecimal(10),
                null, 100, null);
        productDao.save(productWithStock100AndId1);
        cartService.add(cart, 1L, 1);
        int startQuantity = cart.getTotalQuantity();

        cartService.update(cart, 1L, 11);

        assertTrue(cart.getTotalQuantity() == startQuantity + 10);
    }

}
