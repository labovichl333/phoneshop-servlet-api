package com.es.phoneshop.web;

import com.es.phoneshop.model.product.cart.CartService;
import com.es.phoneshop.model.product.cart.DefaultCartService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class MiniCartServlet extends HttpServlet {
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        cartService.getCart(req);
        req.getRequestDispatcher("/WEB-INF/pages/minicart.jsp").include(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        cartService.getCart(req);
        req.getRequestDispatcher("/WEB-INF/pages/minicart.jsp").include(req, resp);
    }
}
