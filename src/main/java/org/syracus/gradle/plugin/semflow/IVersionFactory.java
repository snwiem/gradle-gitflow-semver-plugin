package org.syracus.gradle.plugin.semflow;

import org.eclipse.jgit.lib.Repository;

/**
 * Created by snwiem on 4/28/2017.
 */
public interface IVersionFactory {

    Version getCurrentVersion(Repository repository);
    Version getNextVersion(Repository repository);

}
