package org.syracus.gradle.plugin.semflow;

import org.eclipse.jgit.lib.Repository;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

/**
 * Created by snwiem on 29.04.2017.
 */
public class RepositoryVersionTask extends DefaultTask {

    public static final String TASK_NAME = "repositoryVersion";

    public RepositoryVersionTask() {
        setGroup(SemflowPlugin.GROUP);
        setDescription("Print the latest version found in the GIT repository");
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



            Version repositoryVersion = versionFactory.getCurrentVersion(repository);
            if (null == repositoryVersion) {
                System.out.println("The repository does not contain any valid release version");
            } else {
                System.out.println("Repository version: " + repositoryVersion.toString());
            }
        } catch(Exception e) {
            System.err.println("Failed to retrieve current version from reposiory.");
            e.printStackTrace(System.err);
        }

    }
}
