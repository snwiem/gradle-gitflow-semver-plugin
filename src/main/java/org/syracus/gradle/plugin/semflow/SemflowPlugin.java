package org.syracus.gradle.plugin.semflow;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * Created by snwiem on 4/28/2017.
 */
public class SemflowPlugin implements Plugin<Project> {

    public static final String GROUP = "Semantic Versioning";
    public static final String NAME = "semflow";

    @Override
    public void apply(Project project) {
        project.getExtensions().add(NAME, new SemflowExtension(project));

        //project.getConvention().getPlugins().put("semflow", new SemflowConvention(project));
        project.getTasks().create(ProjectVersionTask.TASK_NAME, ProjectVersionTask.class);
        project.getTasks().create(RepositoryVersionTask.TASK_NAME, RepositoryVersionTask.class);
        //project.getTasks().create(NextVersionTask.TASK_NAME, NextVersionTask.class);

    }

    public static class VersionException extends RuntimeException {
        private static final long serialVersionUID = -1L;

        public VersionException() {
        }

        public VersionException(String message) {
            super(message);
        }

        public VersionException(String message, Throwable cause) {
            super(message, cause);
        }

        public VersionException(Throwable cause) {
            super(cause);
        }
    }

}
