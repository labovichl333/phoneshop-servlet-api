package com.es.phoneshop.model.dao;

public interface Dao<T> {

    void save(T item) throws DaoException;

    T getItem(Long id) throws DaoException;
}
