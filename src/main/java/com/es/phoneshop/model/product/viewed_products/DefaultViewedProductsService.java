package com.es.phoneshop.model.product.viewed_products;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultViewedProductsService implements ViewedProductsService {
    public static final String VIEWED_PRODUCTS_HOLDER_SESSION_ATTRIBUTE = "ViewedProductsHolder";
    private ProductDao productDao = ArrayListProductDao.getInstance();
    private final Lock writeLock = new ReentrantReadWriteLock().writeLock();
    private static final int MAX_COUNT_OF_PRODUCTS = 3;

    private DefaultViewedProductsService() {
    }

    @Override
    public ViewedProductsHolder getViewedProductsHolder(HttpServletRequest request) {
        writeLock.lock();
        try {
            ViewedProductsHolder holder = (ViewedProductsHolder) request.getSession()
                    .getAttribute(VIEWED_PRODUCTS_HOLDER_SESSION_ATTRIBUTE);
            if (holder == null) {
                request.getSession().setAttribute(VIEWED_PRODUCTS_HOLDER_SESSION_ATTRIBUTE,
                        holder = new ViewedProductsHolder(MAX_COUNT_OF_PRODUCTS));
            }
            return holder;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void addViewedProduct(ViewedProductsHolder holder, Long productId) {
        writeLock.lock();
        try {
            List<Product> products = holder.getProducts();
            Optional<Product> addingProduct = products.stream()
                    .filter(product -> productId.equals(product.getId()))
                    .findAny();
            if (addingProduct.isPresent()) {
                products.remove(addingProduct.get());
                products.add(0, addingProduct.get());
            } else {
                if (products.size() < holder.getMaxCountOfSavingProducts()) {
                    products.add(0, productDao.getProduct(productId));
                } else {
                    products.add(0, productDao.getProduct(productId));
                    products.remove(products.size() - 1);
                }
            }
        } finally {
            writeLock.unlock();
        }

    }

    public static DefaultViewedProductsService getInstance() {
        return DefaultViewedProductsService.Holder.INSTANCE;
    }

    private static class Holder {
        static final DefaultViewedProductsService INSTANCE = new DefaultViewedProductsService();
    }
}
