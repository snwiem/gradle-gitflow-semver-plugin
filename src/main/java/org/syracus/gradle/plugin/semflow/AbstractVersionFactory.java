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
public abstract class AbstractVersionFactory implements IVersionFactory {



    private final GitFlowConfig flowConfig;
    private final String initialVersion;

    AbstractVersionFactory(GitFlowConfig flowConfig, String initialVersion) {
        this.flowConfig = flowConfig;
        this.initialVersion = initialVersion;
    }

    public GitFlowConfig getFlowConfig() {
        return flowConfig;
    }

    public String getInitialVersion() {
        return initialVersion;
    }

    @Override
    public Version getCurrentVersion(Repository repository) {
        try {
            Branch branch = GitRepository.getBranch(repository);
            Map<ObjectId, Set<Tag>> allTagsWithPrefix = GitRepository.getAllTagsWithPrefix(repository, getFlowConfig().getVersiontag());
            Version currentVersion = GitRepository.findLatestTagVersionWithPrefix(repository, allTagsWithPrefix, getFlowConfig().getVersiontag());
            return currentVersion;
        } catch(IOException e) {
            throw new SemFlowException("Failed to get current version", e);
        } catch(GitAPIException e) {
            throw new SemFlowException("Failed to get current version", e);
        }
    }

    @Override
    public Version getNextVersion(Repository repository) {
        try {
            Branch branch = GitRepository.getBranch(repository);
            Map<ObjectId, Set<Tag>> allTagsWithPrefix = GitRepository.getAllTagsWithPrefix(repository, getFlowConfig().getVersiontag());
            Version currentVersion = GitRepository.findLatestTagVersionWithPrefix(repository, allTagsWithPrefix, getFlowConfig().getVersiontag());
            Version nextVersion = buildNextVersion(branch, currentVersion);
            return nextVersion;
        } catch(IOException e) {
            throw new SemFlowException("Failed to get current version", e);
        } catch(GitAPIException e) {
            throw new SemFlowException("Failed to get current version", e);
        }

    }

    abstract Version buildNextVersion(Branch branch, Version lastVersion);
}
