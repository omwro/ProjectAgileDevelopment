package com.example.animalcrash.repository;

import com.example.animalcrash.model.Dier;
import java.util.ArrayList;

/**
 * @author      MEHMET TETIK
 * @version     1.0
 *
 * Purpose      Dieren repository
 *
 */

public class DierRepository implements RepositoryManager<Dier> {

    private static ArrayList<Dier> dieren = new ArrayList<>();

    @Override
    public Dier get(ArrayList<Dier> objects, String name) {
        for (Dier dier : objects) {
            if (dier.getNaam().equals(name)) {
                return dier;
            }
        }

        return null;
    }

    public Dier getById(ArrayList<Dier> objects, int animalId) {
        for (Dier dier : objects) {
            if (dier.getId() == animalId) {
                return dier;
            }
        }

        return null;
    }

    @Override
    public void add(Dier object) {
        dieren.add(object);
    }

    @Override
    public void update(Dier object, Dier newObject) {
        remove(object);
        add(newObject);
    }

    @Override
    public void remove(Dier object) {
        dieren.remove(object);
    }

    @Override
    public ArrayList<Dier> list() {
        return dieren;
    }
}
