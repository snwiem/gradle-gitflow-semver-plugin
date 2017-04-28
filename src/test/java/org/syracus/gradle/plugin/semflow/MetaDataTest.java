package org.syracus.gradle.plugin.semflow;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by snwiem on 4/27/2017.
 */
public class MetaDataTest {

    @Test
    public void testFromStringEmpty() {
        String input = "";
        MetaData version = MetaData.fromString(input);
        String output = version.toString();
        System.out.println(output);
        assertEquals(input, output);
    }

    @Test
    public void testFromStringNotEmpty() {
        String input = "alpha";
        MetaData version = MetaData.fromString(input);
        String output = version.toString();
        assertEquals(input, output);
    }

    @Test
    public void testFromStringMultiTokens() {
        String input = "alpha.beta";
        MetaData version = MetaData.fromString(input);
        String output = version.toString();
        assertEquals(input, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromStringInvalidIdentifier() {
        String input = "^alpha$";
        MetaData version = MetaData.fromString(input);
    }

    @Test
    public void testFromStringValidNumberZero() {
        String input = "0";
        MetaData version = MetaData.fromString(input);
        String output = version.toString();
        assertEquals(input, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromStringInvalidNumber2() {
        String input = "01";
        MetaData version = MetaData.fromString(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromStringInvalidNumber3() {
        String input = "alpha.01";
        MetaData version = MetaData.fromString(input);
    }

    @Test
    public void testIncrementNoNumber() {
        String input = "alpha";
        MetaData version = MetaData.fromString(input);
        version = version.increment();
        String output = version.toString();
        assertEquals("alpha.1", output);
    }

    @Test
    public void testIncrementLastNumber() {
        String input = "alpha.1";
        MetaData version = MetaData.fromString(input);
        version = version.increment();
        String output = version.toString();
        assertEquals("alpha.2", output);
    }

    @Test
    public void testIncrementMidNumber() {
        String input = "alpha.1.beta";
        MetaData version = MetaData.fromString(input);
        version = version.increment();
        String output = version.toString();
        assertEquals("alpha.2.beta", output);
    }

    @Test
    public void testIncrementFirstNumber() {
        String input = "1.beta";
        MetaData version = MetaData.fromString(input);
        version = version.increment();
        String output = version.toString();
        assertEquals("2.beta", output);
    }
}
