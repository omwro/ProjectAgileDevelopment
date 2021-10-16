package com.example.animalcrash.repository;

import com.example.animalcrash.util.Map;

import java.util.ArrayList;

/**
 * @author      MEHMET TETIK
 * @version     1.0
 *
 * Purpose      Map repository
 *              Placed animals on the map
 *
 */

public class MapRepository implements RepositoryManager<Map> {

    private static ArrayList<Map> geplaatsteDieren = new ArrayList<>();

    @Override
    public Map get(ArrayList<Map> objects, String name) {
        return null;
    }

    public Map getById(ArrayList<Map> objects, int placedAnimalId) {
        for (Map geplaatsteDier : objects) {
            if (geplaatsteDier.getId() == placedAnimalId) {
                return geplaatsteDier;
            }
        }

        return null;
    }

    @Override
    public void add(Map object) {
        geplaatsteDieren.add(object);
    }

    @Override
    public void update(Map object, Map newObject) {
        remove(object);
        add(newObject);
    }

    @Override
    public void remove(Map object) {
        geplaatsteDieren.remove(object);
    }

    @Override
    public ArrayList<Map> list() {
        return geplaatsteDieren;
    }
}
