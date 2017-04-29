package org.syracus.gradle.plugin.semflow;


import org.gradle.api.Project;

import java.io.IOException;

/**
 * Created by snwiem on 29.04.2017.
 */
public class SemflowExtension {

    private String initialVersion = DefaultVersionFactory.DEFAULT_INITIAL_VERSION;
    private String alphaModifier = DefaultVersionFactory.DEFAULT_ALPHA_MODIFIER;
    private String betaModifier = DefaultVersionFactory.DEFAULT_BETA_MODIFIER;
    private String dirtyIdentifier = DefaultVersionFactory.DEFAULT_DIRTY_IDENTIFIER;
    private final Project project;

    public SemflowExtension(Project project) {
        this.project = project;
    }

    public String getInitialVersion() {
        return initialVersion;
    }

    public void setInitialVersion(String initialVersion) {
        this.initialVersion = initialVersion;
    }

    public String getAlphaModifier() {
        return alphaModifier;
    }

    public void setAlphaModifier(String alphaModifier) {
        this.alphaModifier = alphaModifier;
    }

    public String getBetaModifier() {
        return betaModifier;
    }

    public void setBetaModifier(String betaModifier) {
        this.betaModifier = betaModifier;
    }

    public String getDirtyIdentifier() {
        return dirtyIdentifier;
    }

    public void setDirtyIdentifier(String dirtyIdentifier) {
        this.dirtyIdentifier = dirtyIdentifier;
    }

    public Version version() {
        try {
            return GitRepository.getNextRepositoryVersion(GitRepository.getRepository(project), this);
        }catch(IOException e) {
            throw new SemflowPlugin.VersionException(e);
        }
    }
}
