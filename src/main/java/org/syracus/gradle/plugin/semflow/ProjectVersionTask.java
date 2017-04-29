package org.syracus.gradle.plugin.semflow;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

/**
 * Created by snwiem on 29.04.2017.
 */
public class ProjectVersionTask extends DefaultTask {

    public static final String TASK_NAME = "projectVersion";

    public ProjectVersionTask(){
        setGroup(SemflowPlugin.GROUP);
        setDescription("Print the version as specified in the project.");
    }

    @TaskAction
    public void printVersion() {
        System.out.println("Project version: " + getProject().getVersion());
    }
}
