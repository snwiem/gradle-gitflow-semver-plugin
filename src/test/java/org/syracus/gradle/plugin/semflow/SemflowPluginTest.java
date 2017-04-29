package org.syracus.gradle.plugin.semflow;

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Test;

import java.io.File;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by snwiem on 29.04.2017.
 */
public class SemflowPluginTest {

    @Test
    public void testPluginApply() {
        Project project = ProjectBuilder.builder()
                .withProjectDir(new File("C:\\Users\\snwiem\\Sources\\semflow-test"))
                .build();
        project.apply(x -> {
            x.plugin("semver");
        });
        Task task = project.getTasks().findByName("semver");
        assertTrue(task instanceof SemflowPlugin);
    }
}
