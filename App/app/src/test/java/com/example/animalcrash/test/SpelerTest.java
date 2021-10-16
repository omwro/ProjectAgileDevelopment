package com.example.animalcrash.test;

import com.example.animalcrash.model.Dier;
import com.example.animalcrash.model.Speler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * @author      MEHMET TETIK
 * @version     1.0
 *
 * Purpose      Player class testing
 *
 */

public class SpelerTest {

    private Speler speler;
    private ArrayList<Dier> dieren;

    @Before
    public void setUp() throws Exception {
        Dier dier1 = new Dier();
        Dier dier2 = new Dier();

        dier1.setId(5);
        dier1.setNaam("Aap");

        dier2.setId(6);
        dier2.setNaam("Zebra");

        dieren = new ArrayList<>();
        dieren.add(dier1);
        dieren.add(dier2);

        speler = new Speler();
        speler.setId(1);
        speler.setGebruikersnaam("Test");
        speler.setGeslacht("Man");
        speler.setGeld(50);
        speler.setSpeeltijd(100);
        speler.setAvatar("http://avatarurl.com");
        speler.setOnline(1);
        speler.setLaatstGezien("2019-04-23 13:27:56");
        speler.setActief(1);
        speler.setXP(500);
        speler.setDieren(dieren);
    }

    @After
    public void tearDown() throws Exception {
        speler = null;
    }

    @Test
    public void getIdTest() {
        int expected = 1;
        int result = speler.getId();

        assertEquals(expected, result);
    }

    @Test
    public void getGebruikersnaamTest() {
        String expected = "Test";
        String result = speler.getGebruikersnaam();

        assertEquals(expected, result);
    }

    @Test
    public void getGeslachtTest() {
        String expected = "Man";
        String result = speler.getGeslacht();

        assertEquals(expected, result);
    }

    @Test
    public void getGeldTest() {
        int expected = 50;
        int result = speler.getGeld();

        assertEquals(expected, result);
    }

    @Test
    public void getSpeeltijdTest() {
        int expected = 100;
        int result = speler.getSpeeltijd();

        assertEquals(expected, result);
    }

    @Test
    public void getAvatarTest() {
        String expected = "http://avatarurl.com";
        String result = speler.getAvatar();

        assertEquals(expected, result);
    }

    @Test
    public void getOnlineTest() {
        int expected = 1;
        int result = speler.isOnline();

        assertEquals(expected, result);
    }

    @Test
    public void getLaatstGezienTest() {
        String expected = "23-04-2019 - 13:27:56";
        String result = speler.getLaatstGezien();

        assertEquals(expected, result);
    }

    @Test
    public void getActiefTest() {
        int expected = 1;
        int result = speler.isActief();

        assertEquals(expected, result);
    }

    @Test
    public void getXPTest() {
        int expected = 500;
        int result = speler.getXP();

        assertEquals(expected, result);
    }

    @Test
    public void getDierenTest() {
        ArrayList expected = dieren;
        ArrayList result = speler.getDieren();

        assertEquals(expected, result);
    }
}