package com.es.phoneshop.model.product;

import java.util.List;

public interface ProductDao {
    Product getProduct(Long id) throws ProductNotFoundExeption;
    List<Product> findProducts();
    void save(Product product) throws IncorrectIdExeption;
    void delete(Long id) throws ProductNotFoundExeption;
}
