package com.company.ReneSerulleU1Capstone.dao;

import com.company.ReneSerulleU1Capstone.model.Item;

import java.util.List;

/**
 * Base interface for Items Data Access object
 * @param <T>: T is a generic class representing the DTO
 */
public interface ItemDao<T> {
    T add(T t);
    T find(Long id);
    List<T> findAll();
    void update(T t);
    void delete(Long id);
    Long countId(Long id);
}
