package com.es.phoneshop.model.product.viewed_products;

import javax.servlet.http.HttpServletRequest;

public interface ViewedProductsService {
    ViewedProductsHolder getViewedProductsHolder(HttpServletRequest request);

    void addViewedProduct(ViewedProductsHolder holder, Long productId);
}
