package org.syracus.gradle.plugin.semflow;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;

/**
 * Created by snwiem on 4/28/2017.
 */
public class GitFlowConfig {

    static final String CONFIG_SECTION_GITFLOW = "gitflow";
    static final String CONFIG_SECTION_BRANCH = "branch";
    static final String CONFIG_SECTION_PREFIX = "prefix";
    static final String CONFIG_SECTION_PATH = "path";

    static final String BRANCH_NAME_MASTER = "master";
    static final String BRANCH_NAME_DEVELOP = "develop";
    static final String BRANCH_NAME_HOTFIX = "hotfix";
    static final String BRANCH_NAME_RELEASE = "release";
    static final String BRANCH_NAME_FEATURE = "feature";
    static final String BRANCH_NAME_BUGFIX = "bugfix";
    static final String BRANCH_NAME_SUPPORT = "support";

    static final String CONFIG_NAME_VERSIONTAG = "versiontag";
    static final String CONFIG_NAME_HOOKS = "hooks";

    private final String master;
    private final String develop;

    private final String feature;
    private final String bugfix;
    private final String release;
    private final String hotfix;
    private final String support;

    private final String versiontag;
    private final String hooks;

    private GitFlowConfig(String master, String develop, String feature, String bugfix, String release, String hotfix, String support, String versiontag, String hooks) {
        this.master = master;
        this.develop = develop;
        this.feature = feature;
        this.bugfix = bugfix;
        this.release = release;
        this.hotfix = hotfix;
        this.support = support;
        this.versiontag = versiontag;
        this.hooks = hooks;
    }

    public String getMaster() {
        return master;
    }

    public String getDevelop() {
        return develop;
    }

    public String getFeature() {
        return feature;
    }

    public String getBugfix() {
        return bugfix;
    }

    public String getRelease() {
        return release;
    }

    public String getHotfix() {
        return hotfix;
    }

    public String getSupport() {
        return support;
    }

    public String getVersiontag() {
        return versiontag;
    }

    public String getHooks() {
        return hooks;
    }
    /*
    public static GitFlowConfig fromRepositoryConfig(StoredConfig config) {
        String master = config.getString(GitFlowConfig.CONFIG_SECTION_GITFLOW, GitFlowConfig.CONFIG_SECTION_BRANCH, GitFlowConfig.BRANCH_NAME_MASTER);
        String develop = config.getString(GitFlowConfig.CONFIG_SECTION_GITFLOW, GitFlowConfig.CONFIG_SECTION_BRANCH, GitFlowConfig.BRANCH_NAME_DEVELOP);

        String bugfix = config.getString(GitFlowConfig.CONFIG_SECTION_GITFLOW, GitFlowConfig.CONFIG_SECTION_PREFIX, GitFlowConfig.BRANCH_NAME_BUGFIX);
        String feature = config.getString(GitFlowConfig.CONFIG_SECTION_GITFLOW, GitFlowConfig.CONFIG_SECTION_BRANCH, GitFlowConfig.BRANCH_NAME_FEATURE);
        String hotfix = config.getString(GitFlowConfig.CONFIG_SECTION_GITFLOW, GitFlowConfig.CONFIG_SECTION_BRANCH, GitFlowConfig.BRANCH_NAME_HOTFIX);
        String release = config.getString(GitFlowConfig.CONFIG_SECTION_GITFLOW, GitFlowConfig.CONFIG_SECTION_BRANCH, GitFlowConfig.BRANCH_NAME_RELEASE);
        String support = config.getString(GitFlowConfig.CONFIG_SECTION_GITFLOW, GitFlowConfig.CONFIG_SECTION_BRANCH, GitFlowConfig.BRANCH_NAME_SUPPORT);

        String versiontag = config.getString(GitFlowConfig.CONFIG_SECTION_GITFLOW, GitFlowConfig.CONFIG_SECTION_BRANCH, GitFlowConfig.CONFIG_NAME_VERSIONTAG);
        String hooks = config.getString(GitFlowConfig.CONFIG_SECTION_GITFLOW, GitFlowConfig.CONFIG_SECTION_PATH, GitFlowConfig.CONFIG_NAME_HOOKS);

        return new GitFlowConfig(master, develop, feature, bugfix, release, hotfix, support, versiontag, hooks);
    }
    */

    public boolean isMaster(Branch branch) {
        return branch.getFullName().equalsIgnoreCase(getMaster());
    }

    public boolean isDevelop(Branch branch) {
        return branch.getFullName().equalsIgnoreCase(getDevelop());
    }

    public boolean isHotfix(Branch branch) {
        return branch.getPrefix().equalsIgnoreCase(getHotfix());
    }

    public boolean isFeature(Branch branch) {
        return branch.getPrefix().equalsIgnoreCase(getFeature());
    }

    public boolean isRelease(Branch branch) {
        return branch.getPrefix().equalsIgnoreCase(getRelease());
    }

    public boolean isBugfix(Branch branch) {
        return branch.getPrefix().equalsIgnoreCase(getBugfix());
    }

    public boolean isSupport(Branch branch) {
        return branch.getPrefix().equalsIgnoreCase(getSupport());
    }

    public static class Builder {
        private String master;
        private String develop;

        private String feature;
        private String bugfix;
        private String release;
        private String hotfix;
        private String support;

        private String versiontag;
        private String hooks;

        public Builder masterBranch(String master) {
            this.master = master;
            return this;
        }
        public Builder developBranch(String develop) {
            this.develop = develop;
            return this;
        }
        public Builder featurePrefix(String feature) {
            this.feature = feature;
            return this;
        }
        public Builder bugfixPrefix(String bugfix) {
            this.bugfix = bugfix;
            return this;
        }
        public Builder releasePrefix(String release) {
            this.release = release;
            return this;
        }
        public Builder hotfixPrefix(String hotfix) {
            this.hotfix = hotfix;
            return this;
        }
        public Builder supportPrefix(String support) {
            this.support = support;
            return this;
        }
        public Builder versionTag(String versionTag) {
            this.versiontag = versionTag;
            return this;
        }
        public Builder hooksPath(String hooks) {
            this.hooks = hooks;
            return this;
        }
        public Builder fromRepository(Repository repository) {
            if (null == repository)
                throw new IllegalArgumentException("Repository not valid");
            final StoredConfig config = repository.getConfig();
            if (null == config)
                throw new IllegalArgumentException("No configuration found in repository");
            // check if we do have a gitflow section
            if (false == config.getSections().contains(GitFlowConfig.CONFIG_SECTION_GITFLOW)) {
                throw new IllegalArgumentException("Repository does not support gitflow");
            }

            this.master = config.getString(GitFlowConfig.CONFIG_SECTION_GITFLOW, GitFlowConfig.CONFIG_SECTION_BRANCH, GitFlowConfig.BRANCH_NAME_MASTER);
            this.develop = config.getString(GitFlowConfig.CONFIG_SECTION_GITFLOW, GitFlowConfig.CONFIG_SECTION_BRANCH, GitFlowConfig.BRANCH_NAME_DEVELOP);

            this.bugfix = config.getString(GitFlowConfig.CONFIG_SECTION_GITFLOW, GitFlowConfig.CONFIG_SECTION_PREFIX, GitFlowConfig.BRANCH_NAME_BUGFIX);
            this.feature = config.getString(GitFlowConfig.CONFIG_SECTION_GITFLOW, GitFlowConfig.CONFIG_SECTION_PREFIX, GitFlowConfig.BRANCH_NAME_FEATURE);
            this.hotfix = config.getString(GitFlowConfig.CONFIG_SECTION_GITFLOW, GitFlowConfig.CONFIG_SECTION_PREFIX, GitFlowConfig.BRANCH_NAME_HOTFIX);
            this.release = config.getString(GitFlowConfig.CONFIG_SECTION_GITFLOW, GitFlowConfig.CONFIG_SECTION_PREFIX, GitFlowConfig.BRANCH_NAME_RELEASE);
            this.support = config.getString(GitFlowConfig.CONFIG_SECTION_GITFLOW, GitFlowConfig.CONFIG_SECTION_PREFIX, GitFlowConfig.BRANCH_NAME_SUPPORT);

            this.versiontag = config.getString(GitFlowConfig.CONFIG_SECTION_GITFLOW, GitFlowConfig.CONFIG_SECTION_PREFIX, GitFlowConfig.CONFIG_NAME_VERSIONTAG);
            this.hooks = config.getString(GitFlowConfig.CONFIG_SECTION_GITFLOW, GitFlowConfig.CONFIG_SECTION_PATH, GitFlowConfig.CONFIG_NAME_HOOKS);
            return this;
        }
        public GitFlowConfig build() {
            return new GitFlowConfig(master, develop, feature, bugfix, release, hotfix, support, versiontag, hooks);
        }


    }

}
