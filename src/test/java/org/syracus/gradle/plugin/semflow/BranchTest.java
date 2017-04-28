package org.syracus.gradle.plugin.semflow;


import org.junit.Test;

import java.util.Base64;

import static org.junit.Assert.assertEquals;

/**
 * Created by snwiem on 4/28/2017.
 */
public class BranchTest {

    @Test
    public void testGetFullename() {
        String input = "this_is/the_fullname";
        Branch branch = new Branch(input);
        String output = branch.getFullName();
        assertEquals(input, output);
    }

    @Test
    public void testGetPrefix() {
        String input = "prefix/name";
        Branch branch = new Branch(input);
        String output = branch.getPrefix();
        assertEquals("prefix/", output);

        input = "prefix/another_prefix/name";
        branch = new Branch(input);
        output = branch.getPrefix();
        assertEquals("prefix/", output);
    }

    @Test
    public void testGetName() {
        String input = "prefix/name";
        Branch branch = new Branch(input);
        String output = branch.getName();
        assertEquals("name", output);

        input = "prefix/another_prefix/name";
        branch = new Branch(input);
        output = branch.getName();
        assertEquals("another_prefix/name", output);
    }


}
