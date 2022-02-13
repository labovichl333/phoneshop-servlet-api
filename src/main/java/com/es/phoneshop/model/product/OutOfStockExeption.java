package com.es.phoneshop.model.product;

public class OutOfStockExeption extends Exception  {
    private final Product product;
    private final int stockRequested;
    private final int stockAvalible;

    public OutOfStockExeption(Product product, int stockRequested, int stockAvalible) {
        this.product = product;
        this.stockRequested = stockRequested;
        this.stockAvalible = stockAvalible;
    }

    public Product getProduct() {
        return product;
    }

    public int getStockRequested() {
        return stockRequested;
    }

    public int getStockAvalible() {
        return stockAvalible;
    }
}
