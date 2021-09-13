package com.github.lmh01.mgt2mt.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class UtilsTest {

    @Test
    public void getEntriesFromStringTest() {
        ArrayList<String> expected = new ArrayList<>();
        ArrayList<String> actual = Utils.getEntriesFromString("<20><50><a><hello><beta><x345>");
        expected.add("20");
        expected.add("50");
        expected.add("a");
        expected.add("hello");
        expected.add("beta");
        expected.add("x345");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getFirstPartTest() {
        Assertions.assertEquals("louis", Utils.getFirstPart("louis <banana>"));
        Assertions.assertEquals("it's me", Utils.getFirstPart(" it's me <cannon>"));
        Assertions.assertEquals("some_string", Utils.getFirstPart("some_string<bonk>"));
    }

    @Test
    public void checkForMutualEntriesTest() {
        ArrayList<String> t11 = new ArrayList<>();
        t11.add("car");
        t11.add("me");
        ArrayList<String> t12 = new ArrayList<>();
        t12.add("car");
        t12.add("you");
        Assertions.assertTrue(Utils.checkForMutualEntries(t11, t12));
        ArrayList<String> t21 = new ArrayList<>();
        t21.add("mama");
        t21.add("papa");
        ArrayList<String> t22 = new ArrayList<>();
        t22.add("grandma");
        t22.add("grandpa");
        Assertions.assertFalse(Utils.checkForMutualEntries(t21, t22));
    }

    @Test
    public void getPositionInListTest() {
        Assertions.assertEquals(4, Utils.getPositionInList("car", new String[]{"me", "you", "mod", "phone", "car", "tree"}));
        Assertions.assertEquals(1, Utils.getPositionInList("it", new String[]{"ouch", "it", "not", "yes", "tree", "boat"}));
        Assertions.assertEquals(-1, Utils.getPositionInList("youtube", new String[]{"me", "you", "mod", "phone", "car", "tree"}));
    }
}