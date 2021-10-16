package com.example.animalcrash.test;

import com.example.animalcrash.model.SpelerLevel;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SpelerLevelTest {

    SpelerLevel spelerLevel;

    @Test
    public void test1(){
        spelerLevel = new SpelerLevel(250);
        assertEquals(spelerLevel.getXp(), 250);
        assertEquals(spelerLevel.getLevel(), 2);
        assertEquals(spelerLevel.getXpLeft(), 150);
        assertEquals(spelerLevel.getXpNeeded(), 200);
        assertEquals(spelerLevel.getProgress(), 75);
    }

    @Test
    public void test2(){
        spelerLevel = new SpelerLevel(1000);
        assertEquals(spelerLevel.getXp(), 1000);
        assertEquals(spelerLevel.getLevel(), 5);
        assertEquals(spelerLevel.getXpLeft(), 0);
        assertEquals(spelerLevel.getXpNeeded(), 500);
        assertEquals(spelerLevel.getProgress(), 0);
    }

    @Test
    public void test3(){
        spelerLevel = new SpelerLevel(100000);
        assertEquals(spelerLevel.getXp(), 100000);
        assertEquals(spelerLevel.getLevel(), 45);
        assertEquals(spelerLevel.getXpLeft(), 1000);
        assertEquals(spelerLevel.getXpNeeded(), 4500);
        assertEquals(spelerLevel.getProgress(), 22);
    }
}