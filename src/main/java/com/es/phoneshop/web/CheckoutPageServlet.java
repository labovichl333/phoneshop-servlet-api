package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.order.DefaultOrderService;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderService;
import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.model.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.dao.ProductDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;


public class CheckoutPageServlet extends HttpServlet {
    private ProductDao productDao;
    private CartService cartService;
    private OrderService orderService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        orderService = DefaultOrderService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cart cart = cartService.getCart(req);
        req.setAttribute("order", orderService.getOrder(cart));
        req.setAttribute("paymentMethods", orderService.getPaymentMethods());
        req.setAttribute("minDate", getCurrentDate());
        req.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cart cart = cartService.getCart(req);
        Order order = orderService.getOrder(cart);
        Map<String, String> errors = new HashMap<>();
        setRequiredParametr(req, "firstName", errors, order::setFirstName);
        setRequiredParametr(req, "lastName", errors, order::setLastName);
        setRequiredParametr(req, "phone", errors, order::setPhone);
        setRequiredParametr(req, "deliveryAddres", errors, order::setDeliveryAddres);
        setPaymentMethod(req, errors, order);
        setDeliveryDate(req, errors, order);
        req.setAttribute("minDate", getCurrentDate());
        if (errors.isEmpty()) {
            orderService.placeOrder(cart, order);
            resp.sendRedirect(req.getContextPath() + "/order/overview/" + order.getSecureId());
        } else {
            req.setAttribute("errors", errors);
            doGet(req, resp);
        }
    }

    private String getCurrentDate() {
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
        return localDate.format(formatter);
    }

    private void setRequiredParametr(HttpServletRequest request, String parameter, Map<String, String> errors,
                                     Consumer<String> consumer) {
        String value = request.getParameter(parameter);
        if (value == null || value.isEmpty()) {
            errors.put(parameter, "Value is required");
        } else {
            consumer.accept(value);
        }
    }

    private void setPaymentMethod(HttpServletRequest request, Map<String, String> errors, Order order) {
        String value = request.getParameter("paymentMethod");
        if (value == null || value.isEmpty()) {
            errors.put("paymentMethod", "Value is required");
        } else {
            order.setPaymentMethod(PaymentMethod.valueOf(value));
        }
    }

    private void setDeliveryDate(HttpServletRequest request, Map<String, String> errors, Order order) {
        String value = request.getParameter("deliveryDate");
        if (value == null || value.isEmpty()) {
            errors.put("deliveryDate", "Value is required");
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
            LocalDate localDate = LocalDate.parse(value, formatter);
            order.setDeliveryDate(localDate);
            if (localDate.isBefore(LocalDate.now())) {
                errors.put("deliveryDate", "Delivery Date is uncorrect");
            }

        }
    }


}
