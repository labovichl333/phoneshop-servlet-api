package com.es.phoneshop.model.product.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.OutOfStockExeption;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultCartService implements CartService {
    public static final String CART_SESSION_ATTRIBUTE = "cart";
    private ProductDao productDao = ArrayListProductDao.getInstance();
    private final Lock writeLock = new ReentrantReadWriteLock().writeLock();

    private DefaultCartService() {
    }

    @Override
    public Cart getCart(HttpServletRequest request) {
        writeLock.lock();
        try {
            Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
            if (cart == null) {
                request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
            }
            return cart;
        } finally {
            writeLock.unlock();
        }

    }

    @Override
    public void add(Cart cart, Long productId, int quantity) throws OutOfStockExeption {
        writeLock.lock();
        try {
            Product product = productDao.getProduct(productId);
            int countOfProductsInCart = countProductsInCart(cart, product);
            if (product.getStock() >= countOfProductsInCart + quantity) {
                Optional<CartItem> cartItem = cart.getItems().stream()
                        .filter(currentCartItem -> product.equals(currentCartItem.getProduct()))
                        .findAny();
                if (cartItem.isPresent()) {
                    int currentQuantity = cartItem.get().getQuantity();
                    cartItem.get().setQuantity(currentQuantity + quantity);
                } else {
                    cart.getItems().add(new CartItem(product, quantity));
                }
            } else
                throw new OutOfStockExeption(product, quantity, product.getStock() - countOfProductsInCart);
        } finally {
            writeLock.unlock();
        }
    }

    private int countProductsInCart(Cart cart, Product product) {
        return cart.getItems().stream()
                .filter(cartItem -> product.getId().equals(cartItem.getProduct().getId()))
                .mapToInt(CartItem::getQuantity).sum();
    }

    public static DefaultCartService getInstance() {
        return DefaultCartService.Holder.INSTANCE;
    }

    private static class Holder {
        static final DefaultCartService INSTANCE = new DefaultCartService();
    }
}
