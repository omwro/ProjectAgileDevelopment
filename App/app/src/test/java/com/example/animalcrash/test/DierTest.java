package com.example.animalcrash.test;

import com.example.animalcrash.model.Dier;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author      MEHMET TETIK
 * @version     1.0
 *
 * Purpose      Map class testing
 *
 */

public class DierTest {

    private Dier dier;

    @Before
    public void setUp() throws Exception {
        dier = new Dier();
        dier.setId(1);
        dier.setNaam("Olifant");
    }

    @After
    public void tearDown() throws Exception {
        dier = null;
    }

    @Test
    public void getIdTest() {
        int expected = 1;
        int result = dier.getId();

        assertEquals(expected, result);
    }

    @Test
    public void getNaamTest() {
        String expected = "Olifant";
        String result = dier.getNaam();

        assertEquals(expected, result);
    }
}