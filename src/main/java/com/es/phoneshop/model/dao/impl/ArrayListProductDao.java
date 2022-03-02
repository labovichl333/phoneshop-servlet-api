package com.es.phoneshop.model.dao.impl;

import com.es.phoneshop.model.dao.AbstractDao;
import com.es.phoneshop.model.dao.DaoException;
import com.es.phoneshop.model.product.NotExistIdExeption;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.dao.ProductDao;
import com.es.phoneshop.model.product.ProductNotFoundExeption;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayListProductDao extends AbstractDao<Product> implements ProductDao {

    private ArrayListProductDao() {
        items = new ArrayList<>();
    }

    @Override
    public Product getProduct(Long id) throws ProductNotFoundExeption {
        Product product = null;
        try {
            product = super.getItem(id);
        } catch (DaoException e) {
            throw new ProductNotFoundExeption(id);
        }
        return product;
    }

    @Override
    public List<Product> findProductsByAdvancedSearch(String productCode, BigDecimal minPrice, BigDecimal maxPrice, int minStock) {
        String[] productCodeWords;
        if (productCode == null) {
            productCodeWords = new String[]{};
        } else {
            productCodeWords = productCode.replaceAll("\\s+", " ").split("\\s");
        }
        return items.stream()
                .filter(this::productHasPrice)
                .filter(this::productIsInStock)
                .filter(product -> product.getStock()>=minStock)
                .filter(product -> minPrice == null || product.getPrice().compareTo(minPrice) >= 0)
                .filter(product -> maxPrice == null || product.getPrice().compareTo(maxPrice) <= 0)
                .filter(product ->productCodeWords.length==0 || productHasSuchProductCode(productCodeWords,product))
                .collect(Collectors.toList());
    }

    private boolean productHasSuchProductCode( String[] productCodeWords,Product product){
       return Arrays.stream(productCodeWords).anyMatch(productCode -> product.getCode().contains(productCode));
    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        readLock.lock();
        Comparator<Product> comparator = Comparator.comparing(product -> {
            if (SortField.DESCRIPTION == sortField) {
                return (Comparable) product.getDescription();
            } else {
                return (Comparable) product.getPrice();
            }
        });
        try {
            if (query == null || query.isEmpty()) {
                return items.stream()
                        .filter(this::productHasPrice)
                        .filter(this::productIsInStock)
                        .sorted(sortOrder == SortOrder.ASC ? comparator : comparator.reversed())
                        .collect(Collectors.toList());
            } else if (sortField == null) {
                return findProductsByRelevance(query);
            } else /*sortField != null*/ {
                return items.stream()
                        .filter(product -> Arrays.stream(query.split(" ")).anyMatch(queryWord -> product.getDescription().contains(queryWord)))
                        .filter(this::productHasPrice)
                        .filter(this::productIsInStock)
                        .sorted(sortOrder == SortOrder.ASC ? comparator : comparator.reversed())
                        .collect(Collectors.toList());
            }

        } finally {
            readLock.unlock();
        }
    }

    private List<Product> findProductsByRelevance(String query) {
        String[] queryWords = query.split(" ");
        return items.stream()
                .filter(product -> Arrays.stream(queryWords).anyMatch(queryWord -> product.getDescription().contains(queryWord)))
                .filter(this::productHasPrice)
                .filter(this::productIsInStock)
                .sorted((product1, product2) -> (int) (Arrays.stream(queryWords).
                        filter(queryWord -> product2.getDescription().contains(queryWord))
                        .count() - Arrays.stream(queryWords)
                        .filter(queryWord -> product1.getDescription().contains(queryWord))
                        .count()))
                .collect(Collectors.toList());
    }

    private boolean productIsInStock(Product product) {
        return product.getStock() > 0;
    }

    private boolean productHasPrice(Product product) {
        return product.getPrice() != null;
    }

    @Override
    public void save(Product product) throws NotExistIdExeption {
        try {
            super.save(product);
        } catch (DaoException e) {
            throw new NotExistIdExeption();
        }
    }

    @Override
    public void delete(Long id) throws ProductNotFoundExeption {
        writeLock.lock();
        try {
            Product productToRemove = items.stream()
                    .filter(product -> id.equals(product.getId()))
                    .findAny()
                    .orElseThrow(() -> new ProductNotFoundExeption(id));
            items.remove(productToRemove);
        } finally {
            writeLock.unlock();
        }

    }

    public static ArrayListProductDao getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        static final ArrayListProductDao INSTANCE = new ArrayListProductDao();
    }
}
