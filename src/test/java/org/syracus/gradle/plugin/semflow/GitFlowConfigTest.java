package org.syracus.gradle.plugin.semflow;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by snwiem on 4/28/2017.
 */
public class GitFlowConfigTest {
    GitFlowConfig config = null;

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
    }

    @Test
    public void testIsMasterBranch() {
        Branch branch = new Branch("master");
        assertTrue(config.isMaster(branch));
        branch = new Branch("develop");
        assertFalse(config.isMaster(branch));
    }

    @Test
    public void testIsDevelopBranch() {
        Branch branch = new Branch("develop");
        assertTrue(config.isDevelop(branch));
        branch = new Branch("master");
        assertFalse(config.isDevelop(branch));
    }

    @Test
    public void testIsFeatureBranch() {
        Branch branch = new Branch("feature/some_feature");
        assertTrue(config.isFeature(branch));
        branch = new Branch("master/some_feature");
        assertFalse(config.isFeature(branch));
    }
}
