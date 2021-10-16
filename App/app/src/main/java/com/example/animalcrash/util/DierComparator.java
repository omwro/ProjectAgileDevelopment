package com.example.animalcrash.util;

import com.example.animalcrash.model.Dier;
import java.util.Comparator;

/**
 * @author      MEHMET TETIK
 * @version     1.0
 *
 * Purpose      Animal class sorting
 *
 */

public class DierComparator {

    /**
     * Sort by animal name
     *
     * @return
     */
    public Comparator<Dier> sortByName() {
        return new Comparator<Dier>() {
            @Override
            public int compare(Dier o1, Dier o2) {
                return o1.getNaam().compareTo(o2.getNaam());
            }
        };
    }

    /**
     * Sort by total animal caughts
     *
     * @return
     */
    public Comparator<Dier> sortByAmountOfCaught() {
        return new Comparator<Dier>() {
            @Override
            public int compare(Dier o1, Dier o2) {
                return Integer.compare(o2.getAantalKeerGevangen(o2.getId()), o1.getAantalKeerGevangen(o1.getId()));
            }
        };
    }
}
