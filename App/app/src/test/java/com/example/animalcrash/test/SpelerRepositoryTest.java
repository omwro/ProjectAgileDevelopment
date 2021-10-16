package com.example.animalcrash.test;

import com.example.animalcrash.model.Speler;
import com.example.animalcrash.repository.SpelerRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author      MEHMET TETIK
 * @version     1.0
 *
 * Purpose      Player repository class testing
 *
 */

public class SpelerRepositoryTest {

    private SpelerRepository spelerRepository;

    private Speler speler1;
    private Speler speler2;
    private Speler speler3;

    @Before
    public void setUp() throws Exception {
        spelerRepository = new SpelerRepository();

        speler1 = new Speler();
        speler2 = new Speler();
        speler3 = new Speler();

        speler1.setId(10);
        speler1.setGebruikersnaam("Speler 1");
        speler1.setGeslacht("Man");
        speler1.setGeld(50);
        speler1.setSpeeltijd(50);

        speler2.setId(11);
        speler2.setGebruikersnaam("Speler 2");
        speler2.setGeslacht("Man");
        speler2.setGeld(150);
        speler2.setSpeeltijd(150);

        speler3.setId(12);
        speler3.setGebruikersnaam("Speler 3");
        speler3.setGeslacht("Vrouw");
        speler3.setGeld(250);
        speler3.setSpeeltijd(250);

        spelerRepository.add(speler1);
        spelerRepository.add(speler2);
        spelerRepository.add(speler3);
    }

    @After
    public void tearDown() throws Exception {
        spelerRepository = null;
        speler1 = null;
        speler2 = null;
        speler3 = null;
    }

    @Test
    public void getSpelerTest() {
        String expected = speler1.toString();
        String result = spelerRepository.get(spelerRepository.list(), "Speler 1").toString();

        assertEquals(expected, result);
    }

    @Test
    public void getSpelerByIdTest() {
        String expected = speler2.toString();
        String result = spelerRepository.getById(spelerRepository.list(), 11).toString();

        assertEquals(expected, result);
    }

    @Test
    public void updateSpelerTest() {
        speler2.setId(5);
        spelerRepository.update(speler2, speler2);

        String expected = speler2.toString();
        String result = spelerRepository.getById(spelerRepository.list(), 5).toString();

        assertEquals(expected, result);
    }

    /*
    @Test
    public void getSpelersTest() {
        List<Speler> spelers = Arrays.asList(speler1, speler2, speler3);

        ArrayList<Speler> expected = new ArrayList<>();
        expected.addAll(spelers);
        ArrayList<Speler> result = spelerRepository.list();

        assertEquals(expected, result);
    }
    */
}