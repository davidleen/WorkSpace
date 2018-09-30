package com.giants3.hd.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by david on 2015/9/30.
 */
public class UnitUtilsTest {

    @Test
    public void testKgToPound() throws Exception {

      float pound=  UnitUtils.kgToPound(100);

        assertEquals(225, pound,4.4);
    }

    @Test
    public void testInchToCm() throws Exception {
        float cm=  UnitUtils.inchToCm(100);

        assertEquals(250, cm,0);
    }

    @Test
    public void testCmToInch() throws Exception {
        float inch=  UnitUtils.cmToInch(100);

        assertEquals(39.37, inch,0);
    }

    @Test
    public void testVolumeMeterToInch() throws Exception {

        float inch=  UnitUtils.volumeMeterToInch(100);

        assertEquals(3531, inch,0);

    }
}