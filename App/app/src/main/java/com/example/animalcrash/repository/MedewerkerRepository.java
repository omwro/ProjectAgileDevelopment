package com.example.animalcrash.repository;

import com.example.animalcrash.model.Medewerker;
import java.util.ArrayList;

/**
 * @author      MEHMET TETIK
 * @version     1.0
 *
 * Purpose      Medewerker repository
 *
 */

public class MedewerkerRepository implements RepositoryManager<Medewerker> {

    private static ArrayList<Medewerker> medewerkers = new ArrayList<>();

    @Override
    public Medewerker get(ArrayList<Medewerker> objects, String username) {
        for (Medewerker medewerker : objects) {
            if (medewerker.getGebruikersnaam().equals(username)) {
                return medewerker;
            }
        }

        return null;
    }

    public Medewerker getById(ArrayList<Medewerker> objects, int userId) {
        for (Medewerker medewerker : objects) {
            if (medewerker.getId() == userId) {
                return medewerker;
            }
        }

        return null;
    }

    @Override
    public void add(Medewerker object) {
        medewerkers.add(object);
    }

    @Override
    public void update(Medewerker object, Medewerker newObject) {
        remove(object);
        add(newObject);
    }

    @Override
    public void remove(Medewerker object) {
        medewerkers.remove(object);
    }

    @Override
    public ArrayList<Medewerker> list() {
        return medewerkers;
    }
}
