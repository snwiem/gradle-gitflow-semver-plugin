package org.syracus.gradle.plugin.semflow;

/**
 * Created by snwiem on 4/27/2017.
 */
public class Version implements Comparable<Version> {

    private static final String PREFIX_PRE_RELEASE = "-";
    private static final String PREFIX_BUILD_META = "+";

    private final ReleaseVersion release;
    private final MetaData preRelease;
    private final MetaData build;

    Version(ReleaseVersion release, MetaData preRelease, MetaData build) {
        this.release = release;
        this.preRelease = preRelease;
        this.build = build;
    }

    Version(ReleaseVersion release, MetaData preRelease) {
        this(release, preRelease, MetaData.NULL);
    }

    Version(ReleaseVersion release) {
        this(release, MetaData.NULL, MetaData.NULL);
    }

    public String getReleaseVersion() {
        return release.toString();
    }

    public String getPreReleaseVersion() {
        return preRelease.toString();
    }

    public String getBuildMeta() {
        return build.toString();
    }

    public Version incrementMajorVersion() {
        return new Version(release.incrementMajor());
    }

    public Version incrementMajorVersion(String preRelease) {
        return new Version(release.incrementMajor(), MetaData.fromString(preRelease));
    }

    public Version incrementMinorVersion() {
        return new Version(release.incrementMinor());
    }

    public Version incrementMinorVersion(String preRelease) {
        return new Version(release.incrementMinor(), MetaData.fromString(preRelease));
    }

    public Version incrementPatchVersion() {
        return new Version(release.incrementPatch());
    }

    public Version incrementPatchVersion(String preRelease) {
        return new Version(release.incrementPatch(), MetaData.fromString(preRelease));
    }

    public Version incrementPreReleaseVersion() {
        return new Version(release, preRelease.increment());
    }

    public Version incrementPreReleaseVersion(String buildMeta) {
        return new Version(release, preRelease, MetaData.fromString(buildMeta));
    }

    public Version incrementBuildMeta() {
        return new Version(release, preRelease, build.increment());
    }

    public Version setPreReleaseVersion(String preRelease) {
        return new Version(release, MetaData.fromString(preRelease));
    }

    public Version setBuildMeta(String buildMeta) {
        return new Version(release, preRelease, MetaData.fromString(buildMeta));
    }

    public int getMajorVersion() {
        return release.getMajor();
    }

    public int getMinorVersion() {
        return release.getMinor();
    }

    public int getPathVersion() {
        return release.getPatch();
    }

    public boolean isGreaterThan(Version other) {
        return compareTo(other) > 0;
    }

    public boolean isGreaterThanOrEqualTo(Version other) {
        return compareTo(other) >= 0;
    }

    public boolean isLowerThan(Version other) {
        return compareTo(other) < 0;
    }

    public boolean isLowerThanOrEqualTo(Version other) {
        return compareTo(other) <= 0;
    }

    public static class Builder {

        private String release;
        private String preRelease;
        private String buildMeta;

        public Builder() {
        }

        public Builder(String version) {
            this.release = version;
        }

        public Builder release(String version) {
            this.release = version;
            return this;
        }

        public Builder prerelease(String version) {
            this.preRelease = version;
            return this;
        }

        public Builder meta(String version) {
            this.buildMeta = version;
            return this;
        }

        public Version build() {
            StringBuilder sb = new StringBuilder();
            if (!isNullOrEmpty(release))
                sb.append(release);
            if (!isNullOrEmpty(preRelease))
                sb.append(PREFIX_PRE_RELEASE).append(preRelease);
            if (!isNullOrEmpty(buildMeta))
                sb.append(PREFIX_BUILD_META).append(buildMeta);
            String versionStr = sb.toString();
            return Version.fromString(versionStr);
        }


    }

    public static Version fromString(String versionStr) {
        MetaData buildMeta = MetaData.NULL;
        MetaData preRelease = MetaData.NULL;
        ReleaseVersion release = null;

        int idx = versionStr.indexOf(PREFIX_BUILD_META);
        if (-1 != idx) {
            String versionPart = versionStr.substring(idx+1);
            buildMeta = MetaData.fromString(versionPart);
            versionStr = versionStr.substring(0, idx);
        }

        idx = versionStr.indexOf(PREFIX_PRE_RELEASE);
        if (-1 != idx) {
            String versionPart = versionStr.substring(idx+1);
            preRelease = MetaData.fromString(versionPart);
            versionStr = versionStr.substring(0, idx);
        }

        release = ReleaseVersion.fromString(versionStr);
        return new Version(release, preRelease, buildMeta);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Version version = (Version) o;

        if (release != null ? !release.equals(version.release) : version.release != null) return false;
        if (preRelease != null ? !preRelease.equals(version.preRelease) : version.preRelease != null) return false;
        return build != null ? build.equals(version.build) : version.build == null;
    }

    @Override
    public int hashCode() {
        int result = 5;
        result = 97 * result + (release != null ? release.hashCode() : 0);
        result = 97 * result + (preRelease != null ? preRelease.hashCode() : 0);
        result = 97 * result + (build != null ? build.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Version o) {
        int result = release.compareTo(o.release);
        if (0 == result) {
            result = preRelease.compareTo(o.preRelease);
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getReleaseVersion());
        String preRelease = getPreReleaseVersion();
        if (!isNullOrEmpty(preRelease))
            sb.append(PREFIX_PRE_RELEASE).append(preRelease);
        String build = getBuildMeta();
        if (!isNullOrEmpty(build))
            sb.append(PREFIX_BUILD_META).append(build);
        return sb.toString();
    }

    private static boolean isNullOrEmpty(final String str) {
        return null == str || str.isEmpty();
    }
}
