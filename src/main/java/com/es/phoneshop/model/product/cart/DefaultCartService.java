package com.es.phoneshop.model.product.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.OutOfStockExeption;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

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
    public void add(Cart cart, Long productId, int quantity) throws OutOfStockExeption, IllegalArgumentException {
        writeLock.lock();
        try {
            Product product = productDao.getProduct(productId);
            int countOfProductsInCart = countProductsInCart(cart, product);
            if (quantity <= 0) {
                throw new IllegalArgumentException();
            }
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
            } else {
                throw new OutOfStockExeption(product, quantity, product.getStock() - countOfProductsInCart);
            }
            recalculateCart(cart);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void update(Cart cart, Long productId, int quantity) throws OutOfStockExeption, IllegalArgumentException {
        writeLock.lock();
        try {
            Product product = productDao.getProduct(productId);
            int countOfProductsInCart = countProductsInCart(cart, product);
            if (quantity <= 0) {
                throw new IllegalArgumentException();
            }

            if (product.getStock() >= quantity) {
                Optional<CartItem> cartItem = cart.getItems().stream()
                        .filter(currentCartItem -> product.equals(currentCartItem.getProduct()))
                        .findAny();
                if (cartItem.isPresent()) {
                    cartItem.get().setQuantity(quantity);
                } else {
                    cart.getItems().add(new CartItem(product, quantity));
                }
            } else {
                throw new OutOfStockExeption(product, quantity, product.getStock() - countOfProductsInCart);
            }
            recalculateCart(cart);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void delete(Cart cart, Long productId) {
        cart.getItems()
                .removeIf(cartItem -> productId.equals(cartItem.getProduct().getId()));
        recalculateCart(cart);
    }

    private int countProductsInCart(Cart cart, Product product) {
        return cart.getItems().stream()
                .filter(cartItem -> product.getId().equals(cartItem.getProduct().getId()))
                .mapToInt(CartItem::getQuantity).sum();
    }

    private void recalculateCart(Cart cart) {
        List<BigDecimal> costOfAllEqualProducts=cart.getItems().stream()
                .map(item->item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .collect(Collectors.toList());
        cart.setTotalCost(costOfAllEqualProducts.stream().reduce(BigDecimal.valueOf(0), BigDecimal::add));
        cart.setTotalQuantity(cart.getItems().stream().mapToInt(CartItem::getQuantity).sum());
    }

    public static DefaultCartService getInstance() {
        return DefaultCartService.Holder.INSTANCE;
    }

    private static class Holder {
        static final DefaultCartService INSTANCE = new DefaultCartService();
    }
}
