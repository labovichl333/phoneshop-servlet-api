package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;


public class ArrayListProductDaoTest {
    private ProductDao productDao;
    private final Currency usd = Currency.getInstance("USD");

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
    }

    @Test
    public void testFindProductsHasCorrectResults() {
        assertFalse(productDao.findProducts().isEmpty());
        Product productOutOfStock = new Product("for test", "Samsung Galaxy S", new BigDecimal(100), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product productWithoutPrice = new Product("for test", "Samsung Galaxy S", null, usd, 8, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(productOutOfStock);
        List<Product> productList = productDao.findProducts();
        assertFalse(productList.contains(productOutOfStock));
        assertFalse(productList.contains(productWithoutPrice));
    }

    @Test
    public void testSaveNewProduct() throws ProductNotFoundExeption {
        Product product = new Product("for test", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);
        Product result = productDao.getProduct(product.getId());
        assertNotNull(result);
        assertEquals("for test", result.getCode());
    }

    @Test
    public void testUpdateProduct() throws ProductNotFoundExeption {
        Product product1 = new Product(0L, "for test 2", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product product2 = new Product(0L, "for test 3", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product1);
        Product result1 = productDao.getProduct(product1.getId());
        assertNotNull(result1);
        assertEquals("for test 2", result1.getCode());
        productDao.save(product2);
        Product result2 = productDao.getProduct(product2.getId());
        assertEquals("for test 3", result2.getCode());
        assertNotNull(result2);
    }

    @Test(expected = NotExistIdExeption.class)
    public void testSaveProductWhithIncorrectId() {
        Product product = (new Product(10000L, "for  test ", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(product);
    }

    @Test(expected = ProductNotFoundExeption.class)
    public void testDeleteProduct() throws ProductNotFoundExeption {
        Product product = (new Product("for delete test ", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(product);
        productDao.delete(product.getId());
        productDao.getProduct(product.getId());
    }
}
