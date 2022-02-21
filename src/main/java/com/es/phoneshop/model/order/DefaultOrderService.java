package com.es.phoneshop.model.order;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.dao.OrderDao;
import com.es.phoneshop.model.dao.impl.ArrayListOrderDao;
import com.es.phoneshop.model.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.dao.ProductDao;
import com.es.phoneshop.model.product.Product;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DefaultOrderService implements OrderService {
    private OrderDao orderDao = ArrayListOrderDao.getInstance();
    private ProductDao productDao = ArrayListProductDao.getInstance();
    private CartService cartService = DefaultCartService.getInstance();

    @Override
    public Order getOrder(Cart cart) {
        Order order = new Order();
        order.setItems(cart.getItems().stream()
                .map(item -> {
                    try {
                        return item.clone();
                    } catch (CloneNotSupportedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList()));
        order.setDeliveryCost(calculateDeliveryCost());
        order.setSubtotal(cart.getTotalCost());
        order.setTotalCost(order.getDeliveryCost().add(order.getSubtotal()));
        return order;
    }

    @Override
    public List<PaymentMethod> getPaymentMethods() {
        return Arrays.asList(PaymentMethod.values());
    }

    @Override
    public void placeOrder(Cart cart, Order order) {
        order.setSecureId(UUID.randomUUID().toString());
        refreshCountOfProductsInStock(order);
        cartService.cleanCart(cart);
        orderDao.save(order);
    }

    public void refreshCountOfProductsInStock(Order order) {
        order.getItems().stream().forEach(item -> {
            Product currentProduct=productDao.getProduct(item.getProduct().getId());
            currentProduct.setStock(item.getProduct().getStock() - item.getQuantity());
            productDao.save(currentProduct);
        });

    }

    private BigDecimal calculateDeliveryCost() {
        return new BigDecimal(5);
    }

    private DefaultOrderService() {
    }

    public static DefaultOrderService getInstance() {
        return DefaultOrderService.Holder.INSTANCE;
    }

    private static class Holder {
        static final DefaultOrderService INSTANCE = new DefaultOrderService();
    }
}
