package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.OutOfStockExeption;
import com.es.phoneshop.model.dao.ProductDao;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;
import com.es.phoneshop.model.product.viewed_products.DefaultViewedProductsService;
import com.es.phoneshop.model.product.viewed_products.ViewedProductsService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;

public class ProductListPageServlet extends HttpServlet {
    private ProductDao productDao;
    private ViewedProductsService viewedProductsService;
    private CartService cartService;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        viewedProductsService = DefaultViewedProductsService.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        String sortField = request.getParameter("sort");
        String sortOrder = request.getParameter("order");

        request.setAttribute("products", productDao.findProducts(query,
                Optional.ofNullable(sortField)
                        .map(param -> SortField.valueOf(param.toUpperCase(Locale.ENGLISH)))
                        .orElse(null),
                Optional.ofNullable(sortOrder)
                        .map(param -> SortOrder.valueOf(param.toUpperCase(Locale.ENGLISH)))
                        .orElse(null)
        ));
        viewedProductsService.getViewedProductsHolder(request);
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String quantityString = req.getParameter("quantity");
        Long productId = Long.valueOf(req.getParameter("productId"));
        Cart cart = cartService.getCart(req);
        int quantity;
        try {
            NumberFormat format = NumberFormat.getInstance(req.getLocale());
            quantity = format.parse(quantityString).intValue();
        } catch (ParseException e) {
            req.setAttribute("errorProductId", productId);
            req.setAttribute("error", "Not a number");
            doGet(req, resp);
            return;
        }
        try {
            cartService.add(cart, productId, quantity);
        } catch (OutOfStockExeption e) {
            req.setAttribute("error", "Out of stock, avalible " + e.getStockAvalible());
            req.setAttribute("errorProductId", productId);
            doGet(req, resp);
            return;
        } catch (IllegalArgumentException illegalArgumentExeption) {
            req.setAttribute("error", "Quantity must be positive");
            req.setAttribute("errorProductId", productId);
            doGet(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/products?message=Product added to cart");
    }

}
