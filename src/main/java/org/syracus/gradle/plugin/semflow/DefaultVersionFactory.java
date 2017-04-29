package org.syracus.gradle.plugin.semflow;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Created by snwiem on 4/28/2017.
 */
public class DefaultVersionFactory extends AbstractVersionFactory implements IVersionFactory {

    public static final String DEFAULT_ALPHA_MODIFIER = "ALPHA";
    public static final String DEFAULT_BETA_MODIFIER = "BETA";
    public static final String DEFAULT_DIRTY_IDENTIFIER = "DIRTY";
    public static final String DEFAULT_INITIAL_VERSION = "0.1.0";


    private final String alphaModifier;
    private final String betaModifier;
    private final String dirtyIdentifier;

    private DefaultVersionFactory(final GitFlowConfig flowConfig, String initialVersion, String alphaModifier, String betaModifier, String dirtyIdentifier) {
        super(flowConfig, initialVersion);
        this.alphaModifier = alphaModifier;
        this.betaModifier = betaModifier;
        this.dirtyIdentifier = dirtyIdentifier;
    }


    public String getAlphaModifier() {
        return alphaModifier;
    }

    public String getBetaModifier() {
        return betaModifier;
    }

    public String getDirtyIdentifier() {
        return dirtyIdentifier;
    }

    @Override
    Version buildNextVersion(Branch branch, Version lastVersion) {
        if (null == lastVersion)
            lastVersion = Version.fromString(getInitialVersion());

        if (getFlowConfig().isMaster(branch)) {
            // in master branch we return the latest release version
            return lastVersion;
        } else if (getFlowConfig().isDevelop(branch)) {
            // in develop branch we return the latest release version with alpha modifier
            return new Version.Builder()
                    .release(lastVersion.getReleaseVersion())
                    .prerelease(getAlphaModifier())
                    .meta(branch.isClean() ? null : getDirtyIdentifier())
                    .build();
        } else if (getFlowConfig().isRelease(branch)) {
            // in release branch we return the latest release version with minor incremented and beta modifier
            /*
            return lastVersion
                    .incrementMinorVersion(getBetaModifier())
                    .setBuildMeta(branch.isClean() ? null : getDirtyIdentifier());
            */
            // the problem here is that we need to manually create the branch using 'git flow hotfix start <version>
            // so we just take the version part and assume the developer sticks to the correct version format
            String versionPartOfReleaseBranch = branch.getName();
            return Version
                    .fromString(versionPartOfReleaseBranch)
                    .setBuildMeta(branch.isClean() ? null : getDirtyIdentifier());
        } else if (getFlowConfig().isHotfix(branch)) {
            // hotfix branches are always spawned from master
            // in hotfix branach we return the latest release version with path incremented (no modifier ?)
            /*
            return lastVersion
                    .incrementPatchVersion()
                    .setBuildMeta(branch.isClean() ? null : getDirtyIdentifier());
            */
            // the problem here is that we need to manually create the branch using 'git flow hotfix start <version>
            // so we just take the version part and assume the developer sticks to the correct version format
            String versionPartOfHotfixBranch = branch.getName();
            return Version
                    .fromString(versionPartOfHotfixBranch)
                    .setBuildMeta(branch.isClean() ? null : getDirtyIdentifier());
        } else if (getFlowConfig().isFeature(branch) || getFlowConfig().isBugfix(branch)) {
            // in bugfix/feature branch we return the latest release version with feature name as modifier
            String featureName = branch.getName();
            return lastVersion
                    .setPreReleaseVersion(featureName)
                    .setBuildMeta(branch.isClean() ? null : getDirtyIdentifier());
        } else if (getFlowConfig().isSupport(branch)) {
            // same as with features
            String supportName = branch.getName();
            return lastVersion
                    .setPreReleaseVersion(supportName)
                    .setBuildMeta(branch.isClean() ? null : getDirtyIdentifier());
        }
        throw new GitRepository.RepositoryException("Unknown branch type");
    }

    public static class Builder {

        private GitFlowConfig flowConfig;
        private String initialVersion = DEFAULT_INITIAL_VERSION;
        private String alphaModifier = DEFAULT_ALPHA_MODIFIER;
        private String betaModifier = DEFAULT_BETA_MODIFIER;
        private String dirtyIdentifier = DEFAULT_DIRTY_IDENTIFIER;

        public Builder(GitFlowConfig flowConfig) {
            if (null == flowConfig)
                throw new IllegalArgumentException("Git Flow configuration can not be NULL");
            this.flowConfig = flowConfig;
        }

        public Builder initialVersion(String versionStr) {
            if (null == versionStr || versionStr.isEmpty())
                throw new IllegalArgumentException("Version can not be null");
            this.initialVersion = versionStr;
            return this;
        }

        public Builder alphaModifier(String modifier) {
            if (null == modifier || modifier.isEmpty())
                throw new IllegalArgumentException("Modifier can not be NULL");
            this.alphaModifier = modifier;
            return this;
        }

        public Builder betaModifier(String modifier) {
            if (null == modifier || modifier.isEmpty())
                throw new IllegalArgumentException("Modifier can not be NULL");
            this.betaModifier = modifier;
            return this;
        }

        public Builder dirtyIdentifier(String identifier) {
            if (null == identifier || identifier.isEmpty())
                throw new IllegalArgumentException("Identifier can not be NULL");
            this.dirtyIdentifier = identifier;
            return this;
        }

        public DefaultVersionFactory build() {
            DefaultVersionFactory factory = new DefaultVersionFactory(this.flowConfig, this.initialVersion, this.alphaModifier, this.betaModifier, this.dirtyIdentifier);
            return factory;
        }
    }

}
