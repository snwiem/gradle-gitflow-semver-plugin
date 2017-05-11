package org.syracus.gradle.plugin.semflow;


import org.gradle.api.Project;

import java.io.IOException;

/**
 * Created by snwiem on 29.04.2017.
 */
public class SemflowExtension {

    private String initial = DefaultVersionFactory.DEFAULT_INITIAL_VERSION;
    private String snapshot = DefaultVersionFactory.DEFAULT_ALPHA_MODIFIER;
    private String candidate = DefaultVersionFactory.DEFAULT_BETA_MODIFIER;
    private String dirty = null; //DefaultVersionFactory.DEFAULT_DIRTY_IDENTIFIER;

    private String master = "master";
    private String develop = "develop";
    private String release = "release/";
    private String hotfix = "hotfix/";
    private String bugfix = "bugfix/";
    private String feature = "feature/";
    private String support = "support/";

    private String releaseTag = "v";

    private final Project project;

    public SemflowExtension(Project project) {
        this.project = project;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }

    public String getCandidate() {
        return candidate;
    }

    public void setCandidate(String candidate) {
        this.candidate = candidate;
    }

    public String getDirty() {
        return dirty;
    }

    public void setDirty(String dirty) {
        this.dirty = dirty;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getDevelop() {
        return develop;
    }

    public void setDevelop(String develop) {
        this.develop = develop;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getHotfix() {
        return hotfix;
    }

    public void setHotfix(String hotfix) {
        this.hotfix = hotfix;
    }

    public String getBugfix() {
        return bugfix;
    }

    public void setBugfix(String bugfix) {
        this.bugfix = bugfix;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getSupport() {
        return support;
    }

    public void setSupport(String support) {
        this.support = support;
    }

    public String getReleaseTag() {
        return releaseTag;
    }

    public void setReleaseTag(String releaseTag) {
        this.releaseTag = releaseTag;
    }

    public Version version() {
        try {
            return GitRepository.getNextRepositoryVersion(GitRepository.getRepository(project), this);
        }catch(IOException e) {
            throw new SemflowPlugin.VersionException(e);
        }
    }
}
