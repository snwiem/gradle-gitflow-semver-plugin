package org.syracus.gradle.plugin.semflow;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by snwiem on 4/27/2017.
 */
class ReleaseVersion implements Comparable<ReleaseVersion> {

    private static final Pattern PATTERN = Pattern.compile("(0|[1-9][0-9]*)\\.(0|[1-9][0-9]*)\\.(0|[1-9][0-9]*)");

    private final int major;
    private final int minor;
    private final int patch;

    ReleaseVersion(int major, int minor, int patch) {
        if (major < 0 || minor < 0 || patch < 0)
            throw new IllegalArgumentException("Major, Minor and Patch versions MUST be non-negative numbers.");

        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    int getMajor() {
        return major;
    }

    int getMinor() {
        return minor;
    }

    int getPatch() {
        return patch;
    }

    ReleaseVersion incrementMajor() {
        return new ReleaseVersion(major+1, 0, 0);
    }

    ReleaseVersion incrementMinor() {
        return new ReleaseVersion(major, minor+1, 0);
    }

    ReleaseVersion incrementPatch() {
        return new ReleaseVersion(major, minor, patch+1);
    }



    @Override
    public int compareTo(ReleaseVersion other) {
        int result = major - other.major;
        if (result == 0) {
            result = minor - other.minor;
            if (result == 0) {
                result = patch - other.patch;
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReleaseVersion that = (ReleaseVersion) o;

        if (major != that.major) return false;
        if (minor != that.minor) return false;
        return patch == that.patch;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + major;
        result = 31 * result + minor;
        result = 31 * result + patch;
        return result;
    }

    @Override
    public String toString() {
        return String.format("%d.%d.%d", major, minor, patch);
    }

    static ReleaseVersion fromString(String versionStr) {
        Matcher m = PATTERN.matcher(versionStr);
        if (false == m.matches())
            throw new IllegalArgumentException("Invalid version format");
        int major = Integer.parseInt(m.group(1));
        int minor = Integer.parseInt(m.group(2));
        int patch = Integer.parseInt(m.group(3));
        return new ReleaseVersion(major, minor, patch);
    }
}
