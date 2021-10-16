package com.example.animalcrash.test;

import com.example.animalcrash.util.DateTimeParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author      MEHMET TETIK
 * @version     1.0
 *
 * Purpose      DateTimeParser Testing
 *
 */

public class DateTimeParserTest {

    private DateTimeParser dateTimeParser;
    private DateTimeParser dateTimeParserWithSetter;

    @Before
    public void setUp() throws Exception {
        dateTimeParser = new DateTimeParser("2019-04-13T20:33:45");
        dateTimeParserWithSetter = new DateTimeParser();
    }

    @After
    public void tearDown() throws Exception {
        dateTimeParser = null;
        dateTimeParserWithSetter = null;
    }

    @Test
    public void setDateTimeWithoutMonthNameTest() {
        dateTimeParserWithSetter.setDateTime("2020-04-13T21:15:00");

        String result = dateTimeParserWithSetter.dateTimeParser(false);
        String expectedResult = "13-04-2020 - 21:15:00";

        assertEquals(expectedResult, result);
    }

    @Test
    public void setDateTimeWithMonthNameTest() {
        dateTimeParserWithSetter.setDateTime("2020-05-13T21:15:00");

        String result = dateTimeParserWithSetter.dateTimeParser(true);
        String expectedResult = "13 Mei 2020 - 21:15:00";

        assertEquals(expectedResult, result);
    }

    @Test
    public void getMonthNameTest() {
        String result = dateTimeParser.getMonth(true);
        String expectedResult = "April";

        assertEquals(expectedResult, result);
    }

    @Test
    public void getMonthTest() {
        String result = dateTimeParser.getMonth(false);
        String expectedResult = "04";

        assertEquals(expectedResult, result);
    }

    @Test
    public void getDayTest() {
        String result = dateTimeParser.getDay();
        String expectedResult = "13";

        assertEquals(expectedResult, result);
    }

    @Test
    public void getYearTest() {
        String result = dateTimeParser.getYear();
        String expectedResult = "2019";

        assertEquals(expectedResult, result);
    }

    @Test
    public void getFullDateWithoutMonthNameTest() {
        String result = dateTimeParser.dateParser(false);
        String expectedResult = "13-04-2019";

        assertEquals(expectedResult, result);
    }

    @Test
    public void getFullDateWithMonthNameTest() {
        String result = dateTimeParser.dateParser(true);
        String expectedResult = "13 April 2019";

        assertEquals(expectedResult, result);
    }

    @Test
    public void getHoursTest() {
        String result = dateTimeParser.getHours();
        String expectedResult = "20";

        assertEquals(expectedResult, result);
    }

    @Test
    public void getMinutesTest() {
        String result = dateTimeParser.getMinutes();
        String expectedResult = "33";

        assertEquals(expectedResult, result);
    }

    @Test
    public void getSecondsTest() {
        String result = dateTimeParser.getSeconds();
        String expectedResult = "45";

        assertEquals(expectedResult, result);
    }

    @Test
    public void timeParserTest() {
        String result = dateTimeParser.timeParser();
        String expectedResult = "20:33:45";

        assertEquals(expectedResult, result);
    }
}