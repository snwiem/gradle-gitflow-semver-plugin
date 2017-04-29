package org.syracus.gradle.plugin.semflow;

import org.eclipse.jgit.lib.Repository;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

/**
 * Created by snwiem on 29.04.2017.
 */
public class NextVersionTask extends DefaultTask {

    public static final String TASK_NAME = "nextVersion";

    public NextVersionTask() {
        setGroup(SemflowPlugin.GROUP);
        setDescription("Print the next version for the current repository status.");
    }

    @TaskAction
    public void printVersion() {
        SemflowExtension extension = (SemflowExtension)getProject().getExtensions().findByName(SemflowPlugin.NAME);
        try {
            final Repository repository = GitRepository.getRepository(getProject());
            final GitFlowConfig flowConfig = new GitFlowConfig.Builder().fromRepository(repository).build();
            IVersionFactory versionFactory = new DefaultVersionFactory.Builder(flowConfig)
                    .initialVersion(extension.getInitialVersion())
                    .alphaModifier(extension.getAlphaModifier())
                    .betaModifier(extension.getBetaModifier())
                    .dirtyIdentifier(extension.getDirtyIdentifier())
                    .build();

            Version nextVersion = versionFactory.getNextVersion(repository);
            if (null == nextVersion) {
                System.out.println("No next version available. Specify an initial version.");
            } else {
                System.out.println("Next version: " + nextVersion.toString());
            }
        } catch(Exception e) {
            System.err.println("Failed to retrieve current version from reposiory.");
            e.printStackTrace(System.err);
        }

    }
}
