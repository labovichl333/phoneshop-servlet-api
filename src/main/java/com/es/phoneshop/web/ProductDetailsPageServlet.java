package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.OutOfStockExeption;
import com.es.phoneshop.model.dao.ProductDao;
import com.es.phoneshop.model.product.viewed_products.DefaultViewedProductsService;
import com.es.phoneshop.model.product.viewed_products.ViewedProductsHolder;
import com.es.phoneshop.model.product.viewed_products.ViewedProductsService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;


public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao productDao;
    private CartService cartService;
    private ViewedProductsService viewedProductsService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        viewedProductsService = DefaultViewedProductsService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long productId = parseProductId(req);
        ViewedProductsHolder holder = viewedProductsService.getViewedProductsHolder(req);
        viewedProductsService.addViewedProduct(holder, productId);
        req.setAttribute("product", productDao.getProduct(productId));
        req.setAttribute("viewedProducts", viewedProductsService.getViewedProductsHolder(req));
        req.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long productId = parseProductId(req);
        String quantityString = req.getParameter("quantity");
        int quantity;
        try {
            NumberFormat format = NumberFormat.getInstance(req.getLocale());
            quantity = format.parse(quantityString).intValue();
        } catch (ParseException e) {
            req.setAttribute("error", "Not a number");
            doGet(req, resp);
            return;
        }
        Cart cart = cartService.getCart(req);
        try {
            cartService.add(cart, productId, quantity);
        } catch (OutOfStockExeption e) {
            req.setAttribute("error", "Out of stock, avalible " + e.getStockAvalible());
            doGet(req, resp);
            return;
        } catch (IllegalArgumentException illegalArgumentExeption) {
            req.setAttribute("error", "Quantity must be positive");
            doGet(req, resp);
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/products/" + productId + "?message=Product added to cart");
    }

    private Long parseProductId(HttpServletRequest request) {
        String productInfo = request.getPathInfo().substring(1);
        return Long.valueOf(productInfo);
    }
}
