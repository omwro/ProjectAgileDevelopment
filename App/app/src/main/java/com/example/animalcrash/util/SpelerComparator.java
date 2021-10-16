package com.example.animalcrash.util;

import com.example.animalcrash.model.Speler;
import java.util.Comparator;

/**
 * @author      MEHMET TETIK
 * @version     1.0
 *
 * Purpose      Player class sorting
 *
 */

public class SpelerComparator {

    /**
     * Sort by user name
     *
     * @return
     */
    public Comparator<Speler> sortByName() {
        return new Comparator<Speler>() {
            @Override
            public int compare(Speler o1, Speler o2) {
                return o1.getGebruikersnaam().compareTo(o2.getGebruikersnaam());
            }
        };
    }

    /**
     * Sort by caught animals (score)
     *
     * @return
     */
    public Comparator<Speler> sortByScore() {
        return new Comparator<Speler>() {
            @Override
            public int compare(Speler o1, Speler o2) {
                return Integer.compare(o2.getDieren().size(), o1.getDieren().size());
            }
        };
    }
}
