package com.es.phoneshop.model.product.viewed_products;

import com.es.phoneshop.model.product.Product;

import java.util.LinkedList;
import java.util.List;

public class ViewedProductsHolder {
    private int maxCountOfSavingProducts;
    private List<Product> products;

    public ViewedProductsHolder(int maxCountOfSavingProducts) {
        this.products = new LinkedList<>();
        this.maxCountOfSavingProducts = maxCountOfSavingProducts;
    }

    public int getMaxCountOfSavingProducts() {
        return maxCountOfSavingProducts;
    }

    public List<Product> getProducts() {
        return products;
    }
}
