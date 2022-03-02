package com.es.phoneshop.web;

import com.es.phoneshop.model.dao.ProductDao;
import com.es.phoneshop.model.dao.impl.ArrayListProductDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AdvancedSearchPageServlet extends HttpServlet {
    private ProductDao productDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!req.getParameterNames().hasMoreElements()) {
            req.getRequestDispatcher("/WEB-INF/pages/advancedSearch.jsp").forward(req, resp);
            return;
        }
        Map<String, String> errors = new HashMap<>();
        String productCode = req.getParameter("productCode");
        BigDecimal minPrice = getPriceParam("minPrice", req, errors, "minPrice");
        BigDecimal maxPrice = getPriceParam("maxPrice", req, errors, "maxPrice");
        int minStock=getStockParam("minStock",req,errors);
        if(minPrice!=null && maxPrice!=null && minPrice.compareTo(maxPrice)>0){
            errors.put("minMaxPrice","Min Price must be less than Max Price");
        }
        if (errors.isEmpty()) {
            req.setAttribute("products", productDao
                    .findProductsByAdvancedSearch(productCode, minPrice, maxPrice,minStock));
        } else {
            req.setAttribute("errors", errors);
        }
        req.getRequestDispatcher("/WEB-INF/pages/advancedSearch.jsp").forward(req, resp);
    }

    private BigDecimal getPriceParam(String param, HttpServletRequest request, Map<String, String> errors, String errorMassageKey) {
        BigDecimal price = null;
        try {
            price = parsePrice(request.getParameter(param), request.getLocale());
        } catch (ParseException e) {
            errors.put(errorMassageKey, "It is not number");
        }
        return price;
    }

    private BigDecimal parsePrice(String priceStr, Locale locale) throws ParseException {
        if (priceStr == null || priceStr.isEmpty()) {
            return null;
        }
        NumberFormat format = NumberFormat.getInstance(locale);
        BigDecimal price=BigDecimal.valueOf(format.parse(priceStr).doubleValue());
        return price;
    }
    private int getStockParam(String param, HttpServletRequest request, Map<String, String> errors) {
        int stock=0;
        try {
          stock=parseStock(request.getParameter(param), request.getLocale());
        } catch (ParseException e) {
            errors.put("minStock", "It is not number");
        }
        return stock;
    }
    private int parseStock(String stockStr, Locale locale) throws ParseException {
        if (stockStr == null || stockStr.isEmpty()) {
            return 0;
        }
        NumberFormat format = NumberFormat.getInstance(locale);
        int  stock = format.parse(stockStr).intValue();
        return stock;
    }

}
