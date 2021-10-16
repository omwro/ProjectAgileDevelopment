package com.example.animalcrash.repository;

import java.util.ArrayList;

/**
 * @author      MEHMET TETIK
 * @version     1.0
 *
 * Purpose      Repository manager, maintain database data
 *
 */

public interface RepositoryManager<T> {

    /**
     * Get object
     *
     * @param objects ArrayList<T>  Objects
     * @param name String           Name to search
     * @return object Object
     */
    T get(ArrayList<T> objects, String name);

    /**
     * Add object
     *
     * @param object Object     Object to add
     */
    void add(T object);

    /**
     * Update object (delete old object, add new object)
     *
     * @param object Object     Object to be deleted
     * @param newObject Object  Object to be added
     */
    void update(T object, T newObject);

    /**
     * Remove object
     *
     * @param object Object     Object to be deleted
     */
    void remove(T object);

    /**
     * Retrieve all objects
     *
     * @return list ArrayList<T>
     */
    ArrayList<T> list();
}
