package com.es.phoneshop.model.dao;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class AbstractDao<T extends Identifiable> implements Dao<T> {
    protected final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    protected final Lock readLock = readWriteLock.readLock();
    protected final Lock writeLock = readWriteLock.writeLock();
    protected List<T> items;
    protected Long maxId = 0L;

    @Override
    public void save(T item) throws DaoException {
        writeLock.lock();
        try {
            if (item.getId() == null) {
                item.setId(maxId++);
                items.add(item);
            } else {
                Optional<T> productForUpdate = items.stream()
                        .filter(currentProduct -> item.getId().equals(currentProduct.getId()))
                        .findAny();
                if (productForUpdate.isPresent()) {
                    items.set(items.indexOf(productForUpdate.get()), item);
                } else {
                    throw new DaoException();
                }
            }
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public T getItem(Long id) throws DaoException {
        readLock.lock();
        try {
            return items.stream()
                    .filter(order -> id.equals(order.getId()))
                    .findAny()
                    .orElseThrow(DaoException::new);
        } finally {
            readLock.unlock();
        }
    }
}
