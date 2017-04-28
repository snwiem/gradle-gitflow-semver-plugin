package org.syracus.gradle.plugin.semflow;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
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
            log.debug("Is repository clean? " + GitRepository.isClean(repository));

            Map<ObjectId, Set<String>> tags = GitRepository.getAllTags(repository);
            for (Map.Entry<ObjectId, Set<String>> entry : tags.entrySet()) {
                System.out.println("ObjectId: " + entry.getKey().toString());
                for (String tag : entry.getValue()) {
                    System.out.println("\tTag: " + tag);
                }
            }

        } catch(Exception e) {
            log.error("git error", e);
        }
    }
}
