package org.syracus.gradle.plugin.semflow;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

/**
 * Created by snwiem on 4/28/2017.
 */
public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        try {

            Repository repository = GitRepository.getRepository(GitRepository.getCurrentWorkingDirectory());
            GitFlowConfig flowConfig = new GitFlowConfig.Builder().fromRepository(repository).build();
            log.info("Configured release version prefix: " + flowConfig.getVersiontag());
            IVersionFactory versionFactory = new DefaultVersionFactory.Builder(flowConfig).build();
            Version currentVersion = versionFactory.getCurrentVersion(repository);
            log.debug("CurrentVersion: " + currentVersion);
            Version nextVersion = versionFactory.getNextVersion(repository);
            log.debug("NextVersion: " + nextVersion);


        } catch(Exception e) {
            log.error("error", e);
        }
    }
}
