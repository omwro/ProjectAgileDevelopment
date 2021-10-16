package com.example.animalcrash.test;

import com.example.animalcrash.util.TranslateHttpResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author      MEHMET TETIK
 * @version     1.0
 *
 * Purpose      Http response class testing
 *
 */

public class TranslateHttpResponseTest {

    private TranslateHttpResponse translateHttpResponse1;
    private TranslateHttpResponse translateHttpResponse2;

    @Before
    public void setUp() throws Exception {
        translateHttpResponse1 = new TranslateHttpResponse();
        translateHttpResponse1.setResponseCode("404");

        translateHttpResponse2 = new TranslateHttpResponse("201");
    }

    @After
    public void tearDown() throws Exception {
        translateHttpResponse1 = null;
        translateHttpResponse2 = null;
    }

    @Test
    public void translateHttpResponseTest1() {
        String expected = "Niet gevonden";
        String result = translateHttpResponse1.translateHttpResponseCode();

        assertEquals(expected, result);
    }

    @Test
    public void translateHttpResponseTest2() {
        String expected = "Aangemaakt";
        String result = translateHttpResponse2.translateHttpResponseCode();

        assertEquals(expected, result);
    }
}