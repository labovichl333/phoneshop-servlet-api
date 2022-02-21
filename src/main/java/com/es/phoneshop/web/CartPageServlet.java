package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.OutOfStockExeption;
import com.es.phoneshop.model.dao.ProductDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;


public class CartPageServlet extends HttpServlet {
    private ProductDao productDao;
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("cart", cartService.getCart(req));
        req.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] quantitys = req.getParameterValues("quantity");
        String[] productIds = req.getParameterValues("productId");
        Map<Long, String> erors = new HashMap<>();
        Cart cart = cartService.getCart(req);
        for (int i = 0; i < productIds.length; i++) {
            Long productId = Long.valueOf(productIds[i]);
            int quantity;
            try {
                NumberFormat format = NumberFormat.getInstance(req.getLocale());
                quantity = format.parse(quantitys[i]).intValue();
                cartService.update(cart, productId, quantity);
            } catch (ParseException e) {
                erors.put(productId, "Not a number");
            } catch (OutOfStockExeption e) {
                erors.put(productId, "Out of stock, avalible " + e.getStockAvalible());
            } catch (IllegalArgumentException illegalArgumentExeption) {
                erors.put(productId, "Quantity must be of positive");
            }
        }

        if (erors.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart?message=Cart update successfully");
        } else {
            req.setAttribute("errors", erors);
            doGet(req, resp);
        }
    }

}
