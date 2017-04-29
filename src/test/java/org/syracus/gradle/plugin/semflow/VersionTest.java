package org.syracus.gradle.plugin.semflow;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by snwiem on 4/27/2017.
 */
public class VersionTest {

    @Test
    public void testReleaseValid() {
        String input = "0.1.2";
        Version version = Version.fromString(input);
        String output = version.toString();
        assertEquals(input, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseInvalid() {
        String input = "0.1";
        Version version = Version.fromString(input);
        String output = version.toString();
        assertEquals(input, output);
    }

    @Test
    public void testReleasePreReleaseValid() {
        String input = "0.9.0-SNAPSHOT";
        Version version = Version.fromString(input);
        String output = version.toString();
        assertEquals(input, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleasePreReleaseInvalid() {
        String input = "0.9.0-SNAPSHOT.001";
        Version version = Version.fromString(input);
        String output = version.toString();
        assertEquals(input, output);
    }

    @Test
    public void testVersionBuilder() {
        String release = "1.0.0";
        String preRelease = "alpha.1";
        String buildMeta = "dev.sha.45635";
        String expected = release + "-" + preRelease + "+" + buildMeta;

        Version version = new Version.
                Builder()
                    .release(release)
                    .prerelease(preRelease)
                    .meta(buildMeta)
                    .build();
        String output = version.toString();
        assertEquals(expected, output);
    }

    @Test
    public void testIsGreaterThan() {
        Version lowerVersion = Version.fromString("1.0.0-SNAPSHOT");
        Version higherVersion = Version.fromString("1.0.0");
        assertTrue(higherVersion.isGreaterThan(lowerVersion));
    }

    @Test
    public void testIsLowerThan() {
        Version versionToTest = Version.fromString("1.9.2");
        assertTrue(versionToTest.isLowerThan(Version.fromString("1.10.0")));
        assertTrue(versionToTest.isLowerThan(Version.fromString("1.9.3-SNAPSHOT")));
        assertFalse(versionToTest.isLowerThan(Version.fromString("0.1.0")));
    }
}
