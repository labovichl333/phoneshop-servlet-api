package com.es.phoneshop.model.product;

import java.util.List;

public interface ProductDao {
    Product getProduct(Long id) throws ProductNotFoundExeption;

    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);

    void save(Product product) throws NotExistIdExeption;

    void delete(Long id) throws ProductNotFoundExeption;
}
