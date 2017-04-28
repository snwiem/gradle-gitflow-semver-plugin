package org.syracus.gradle.plugin.semflow;

/**
 * Created by snwiem on 4/28/2017.
 */
public class Branch {

    private final String name;

    private static final String BRANCH_NAME_MASTER = "master";
    private static final String BRANCH_NAME_DEVELOP = "develop";
    private static final String BRANCH_NAME_HOTFIX = "hotfix";
    private static final String BRANCH_NAME_RELEASE = "release";
    private static final String BRANCH_NAME_FEATURE = "feature";
    private static final String BRANCH_NAME_BUGFIX = "bugfix";
    private static final String BRANCH_NAME_SUPPORT = "support";

    private static final String PREFIX_HOTFIX = BRANCH_NAME_HOTFIX + "/";
    private static final String PREFIX_RELEASE = BRANCH_NAME_RELEASE + "/";
    private static final String PREFIX_FEATURE = BRANCH_NAME_FEATURE + "/";
    private static final String PREFIX_BUGFIX = BRANCH_NAME_BUGFIX + "/";
    private static final String PREFIX_SUPPORT = BRANCH_NAME_SUPPORT + "/";


    public Branch(String name) {
        if (null == name || name.isEmpty())
            throw new IllegalArgumentException("Branch name can not be empty or null");
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Branch branch = (Branch) o;

        return name.equals(branch.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public boolean isMaster() {
        return name.equalsIgnoreCase(BRANCH_NAME_MASTER);
    }

    public boolean isDevelop() {
        return name.equalsIgnoreCase(BRANCH_NAME_DEVELOP);
    }

    public boolean isHotfix() {
        return name.startsWith(PREFIX_HOTFIX);
    }

    public boolean isFeature() {
        return name.startsWith(PREFIX_FEATURE);
    }

    public boolean isRelease() {
        return name.startsWith(PREFIX_RELEASE);
    }

    public boolean isBugfix() {
        return name.startsWith(PREFIX_BUGFIX);
    }

    public boolean isSupport() {
        return name.startsWith(PREFIX_SUPPORT);
    }
}
