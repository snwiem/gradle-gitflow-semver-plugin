package org.syracus.gradle.plugin.semflow;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by snwiem on 4/28/2017.
 */
public class DefaultVersionFactoryTest {

    GitFlowConfig config = null;
    DefaultVersionFactory factory = null;

    @Before
    public void setup() {
        this.config = new GitFlowConfig.Builder()
                .masterBranch("master")
                .developBranch("develop")
                .releasePrefix("release/")
                .hotfixPrefix("hotfix/")
                .featurePrefix("feature/")
                .bugfixPrefix("bugfix/")
                .supportPrefix("support/")
                .versionTag("release-")
                .hooksPath("some_path")
                .build();

        this.factory = new DefaultVersionFactory.Builder(this.config)
                .alphaModifier("SNAPSHOT")
                .betaModifier("RC")
                .dirtyIdentifier("DIRTY")
                .build();

    }

    @Test
    public void testGetVersionMasterClean() {
        Branch branch = new Branch("master");
        Version lastReleaseVersion = Version.fromString("1.5.34");
        Version expectedVersion = Version.fromString("1.5.34");
        Version masterVersion = this.factory.buildNextVersion(branch, lastReleaseVersion);
        assertEquals(expectedVersion, masterVersion);
    }

    @Test
    public void testGetVersionMasterDirty() {
        Branch branch = new Branch("master", false);
        Version lastReleaseVersion = Version.fromString("1.5.34");
        Version expectedVersion = Version.fromString("1.5.34");
        Version masterVersion = this.factory.buildNextVersion(branch, lastReleaseVersion);
        assertEquals(expectedVersion, masterVersion);
    }

    @Test
    public void testGetVersionDevelopmentClean() {
        Branch branch = new Branch("develop");
        Version lastReleaseVersion = Version.fromString("1.5.34");
        Version expectedVersion = Version.fromString("1.5.34-SNAPSHOT");
        Version masterVersion = this.factory.buildNextVersion(branch, lastReleaseVersion);
        assertEquals(expectedVersion, masterVersion);
    }

    @Test
    public void testGetVersionDevelopmentDirty() {
        Branch branch = new Branch("develop", false);
        Version lastReleaseVersion = Version.fromString("1.5.34");
        Version expectedVersion = Version.fromString("1.5.34-SNAPSHOT+DIRTY");
        Version masterVersion = this.factory.buildNextVersion(branch, lastReleaseVersion);
        assertEquals(expectedVersion, masterVersion);
    }

    @Test
    public void testGetVersionFeatureClean() {
        Branch branch = new Branch("feature/some-feature");
        Version lastReleaseVersion = Version.fromString("1.5.34");
        Version expectedVersion = Version.fromString("1.5.34-some-feature");
        Version masterVersion = this.factory.buildNextVersion(branch, lastReleaseVersion);
        assertEquals(expectedVersion, masterVersion);
    }

    @Test
    public void testGetVersionFeatureDirty() {
        Branch branch = new Branch("feature/some-feature", false);
        Version lastReleaseVersion = Version.fromString("1.5.34");
        Version expectedVersion = Version.fromString("1.5.34-some-feature+DIRTY");
        Version masterVersion = this.factory.buildNextVersion(branch, lastReleaseVersion);
        assertEquals(expectedVersion, masterVersion);
    }

    @Test
    public void testGetVersionHotfixClean() {
        Branch branch = new Branch("hotfix/1.5.35");
        Version lastReleaseVersion = Version.fromString("1.5.34");
        Version expectedVersion = Version.fromString("1.5.35");
        Version hotfixVersion = this.factory.buildNextVersion(branch, lastReleaseVersion);
        assertEquals(expectedVersion, hotfixVersion);
    }

    @Test
    public void testGetVersionHotfixDirty() {
        Branch branch = new Branch("hotfix/1.5.35", false);
        Version lastReleaseVersion = Version.fromString("1.5.34");
        Version expectedVersion = Version.fromString("1.5.35+DIRTY");
        Version hotfixVersion = this.factory.buildNextVersion(branch, lastReleaseVersion);
        assertEquals(expectedVersion, hotfixVersion);
    }

    @Test
    public void testGetVersionReleaseClean() {
        Branch branch = new Branch("release/1.5.35");
        Version lastReleaseVersion = Version.fromString("1.5.34");
        Version expectedVersion = Version.fromString("1.5.35");
        Version hotfixVersion = this.factory.buildNextVersion(branch, lastReleaseVersion);
        assertEquals(expectedVersion, hotfixVersion);
    }

    @Test
    public void testGetVersionReleaseDirty() {
        Branch branch = new Branch("release/1.5.35", false);
        Version lastReleaseVersion = Version.fromString("1.5.34");
        Version expectedVersion = Version.fromString("1.5.35+DIRTY");
        Version hotfixVersion = this.factory.buildNextVersion(branch, lastReleaseVersion);
        assertEquals(expectedVersion, hotfixVersion);
    }
}
