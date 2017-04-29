package org.syracus.gradle.plugin.semflow;

import org.eclipse.jgit.lib.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by snwiem on 4/28/2017.
 */
public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        try {
            System.out.println(args.length);
            if (0 == args.length) {
                Repository repository = GitRepository.getRepository(GitRepository.getCurrentWorkingDirectory());
                Version version = GitRepository.getRepositoryVersion(repository, new SemflowExtension(null));
                log.debug("Current version: " + version.toString());
                Version nextVersion = GitRepository.getNextRepositoryVersion(repository, new SemflowExtension(null));
                log.debug("Next version: " + nextVersion.toString());
            } else {
                for (int i = 0; i < args.length; ++i) {
                    Repository repository = GitRepository.getRepository(args[i]);
                    Version version = GitRepository.getRepositoryVersion(repository, new SemflowExtension(null));
                    if (null == version) {
                        log.warn("No repository version found");
                    } else {
                        log.debug("[" + args[i] + "] Current version: " + version.toString());
                    }

                    Version nextVersion = GitRepository.getNextRepositoryVersion(repository, new SemflowExtension(null));
                    log.debug("[" + args[i] + "] Next version: " + nextVersion.toString());
                }
            }
        } catch(Exception e) {
            log.error("error", e);
        }
    }
}
