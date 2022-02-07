package com.es.phoneshop.model.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();
    private List<Product> products;
    private Long maxId = 0L;

    private ArrayListProductDao() {
        this.products = new ArrayList<>();
    }

    @Override
    public Product getProduct(Long id) throws ProductNotFoundExeption {
        readLock.lock();
        try {
            return products.stream()
                    .filter(product -> id.equals(product.getId()))
                    .findAny()
                    .orElseThrow(() -> new ProductNotFoundExeption(id));
        } finally {
            readLock.unlock();
        }

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
                return products.stream()
                        .filter(this::productHasPrice)
                        .filter(this::productIsInStock)
                        .sorted(sortOrder == SortOrder.ASC ? comparator : comparator.reversed())
                        .collect(Collectors.toList());
            } else if (sortField == null) {
                return findProductsByRelevance(query);
            } else /*sortField != null*/ {
                return products.stream()
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
        return products.stream()
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
        writeLock.lock();
        try {
            if (product.getId() == null) {
                product.setId(maxId++);
                products.add(product);
            } else {
                Optional<Product> productForUpdate = products.stream()
                        .filter(currentProduct -> product.getId().equals(currentProduct.getId()))
                        .findAny();
                if (productForUpdate.isPresent()) {
                    products.set(products.indexOf(productForUpdate.get()), product);
                } else {
                    throw new NotExistIdExeption();
                }
            }
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void delete(Long id) throws ProductNotFoundExeption {
        writeLock.lock();
        try {
            Product productToRemove = products.stream()
                    .filter(product -> id.equals(product.getId()))
                    .findAny()
                    .orElseThrow(() -> new ProductNotFoundExeption(id));
            products.remove(productToRemove);
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
