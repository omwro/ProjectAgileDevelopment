package com.example.animalcrash.repository;

import com.example.animalcrash.model.Speler;
import java.util.ArrayList;

/**
 * @author      MEHMET TETIK
 * @version     1.0
 *
 * Purpose      Speler repository
 *
 */

public class SpelerRepository implements RepositoryManager<Speler> {

    private static ArrayList<Speler> spelers = new ArrayList<>();

    @Override
    public Speler get(ArrayList<Speler> objects, String username) {
        for (Speler speler : objects) {
            if (speler.getGebruikersnaam().equals(username)) {
                return speler;
            }
        }

        return null;
    }

    public Speler getById(ArrayList<Speler> objects, int userId) {
        for (Speler speler : objects) {
            if (speler.getId() == userId) {
                return speler;
            }
        }

        return null;
    }

    @Override
    public void add(Speler object) {
        spelers.add(object);
    }

    @Override
    public void update(Speler object, Speler newObject) {
        remove(object);
        add(newObject);
    }

    @Override
    public void remove(Speler object) {
        spelers.remove(object);
    }

    @Override
    public ArrayList<Speler> list() {
        return spelers;
    }
}
