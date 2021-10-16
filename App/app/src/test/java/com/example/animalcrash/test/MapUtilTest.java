package com.example.animalcrash.test;

import com.example.animalcrash.MapsActivity;
import com.example.animalcrash.util.MapUtil;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class MapUtilTest {

    private MapUtil mapUtil;
    private LatLng loc1;
    private LatLng loc2;
    private LatLng loc3;
    private LatLng loc4;
    private MarkerOptions marker;

    private Date date1 = new Date();
    private Calendar today = Calendar.getInstance();
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @Before
    public void setUp() throws Exception {
        mapUtil = new MapUtil(new MapsActivity());
        date1.setTime(today.getTimeInMillis());
        marker = new MarkerOptions().position(loc1);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void distanceCheck() {
    }



    @Test
    public void timeAfter() {
        boolean expected = false;
        boolean result = mapUtil.timeAfter(date1.getTime());
        assertEquals(expected,result);
    }
}