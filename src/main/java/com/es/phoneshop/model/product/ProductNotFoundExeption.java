package com.es.phoneshop.model.product;

public class ProductNotFoundExeption extends RuntimeException {
    private long id;

    public ProductNotFoundExeption(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
