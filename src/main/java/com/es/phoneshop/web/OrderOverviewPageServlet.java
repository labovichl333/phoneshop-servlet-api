package com.es.phoneshop.web;

import com.es.phoneshop.model.dao.impl.ArrayListOrderDao;
import com.es.phoneshop.model.dao.OrderDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class OrderOverviewPageServlet extends HttpServlet {
    private OrderDao orderDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String secureOrderId = parseOrderSecureId(req);
        req.setAttribute("order", orderDao.getOrderBySecureId(secureOrderId));
        req.getRequestDispatcher("/WEB-INF/pages/orderOverview.jsp").forward(req, resp);
    }

    private String parseOrderSecureId(HttpServletRequest request) {
        return request.getPathInfo().substring(1);
    }

}
