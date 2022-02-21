package com.es.phoneshop.model.dao;

import com.es.phoneshop.model.product.NotExistIdExeption;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductNotFoundExeption;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;

import java.util.List;

public interface ProductDao {
    Product getProduct(Long id) throws ProductNotFoundExeption;

    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);

    void save(Product product) throws NotExistIdExeption;

    void delete(Long id) throws ProductNotFoundExeption;
}
