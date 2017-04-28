package org.syracus.gradle.plugin.semflow;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by snwiem on 4/28/2017.
 */
public class GitRepository {

    private static final Logger log = LoggerFactory.getLogger(GitRepository.class);

    public static File getCurrentWorkingDirectory() {
        File currentWorkingDirectory = Paths.get(".")
                .toAbsolutePath()
                .normalize()
                .toFile();
        return currentWorkingDirectory;
    }

    public static Repository getRepository(String repositoryLocation) throws IOException
    {
        File repositoryLocationPath = new File(repositoryLocation);
        return getRepository(repositoryLocationPath);
    }

    /*
    public static Repository getRepository(Project project) throws IOException {
        File projectDirectory = project.getProjectDir().getAbsoluteFile();
        return getRepository(projectDirectory);
    }
    */

    public static Repository getRepository(File repositoryLocation) throws IOException
    {
        if (false == repositoryLocation.exists()) {
            throw new IOException(String.format("Given repository location %s does not exist.", repositoryLocation));
        }
        if (false == repositoryLocation.isDirectory()) {
            throw new IOException(String.format("Given repository location %s is not a directory", repositoryLocation));
        }
        if (false == repositoryLocation.canRead()) {
            throw new IOException(String.format("Given repository lcoation %s can not be read", repositoryLocation));
        }

        Repository repository = new FileRepositoryBuilder()
                .readEnvironment()
                .findGitDir(repositoryLocation)
                .build();
        return repository;
    }

    public static boolean isClean(Repository repository) throws GitAPIException {
        Git git = new Git(repository);
        Status status = git.status().call();
        return status.isClean();
    }

    public static Branch getBranch(Repository repository) throws IOException {
        String branchName = repository.getBranch();
        return new Branch(branchName);
    }

    public static Map<ObjectId, Set<String>> getAllTags(Repository repository) {
        Map<ObjectId, Set<String>> map = new HashMap<>();
        Map<String,Ref> refs = repository.getTags();
        for (Map.Entry<String, Ref> ref : refs.entrySet()) {
            ObjectId objectId = getIdForRef(repository, ref.getValue());
            if (map.containsKey(objectId)) {
                map.get(objectId).add(ref.getKey());
            } else {
                Set<String> tags = new HashSet<>();
                tags.add(ref.getKey());
                map.put(objectId, tags);
            }
        }
        return map;
    }

    public static ObjectId getIdForRef(Repository repository, Ref ref) {
        Ref peeledRef = repository.peel(ref);
        return null == peeledRef.getPeeledObjectId() ? ref.getObjectId() : ref.getPeeledObjectId();
    }


}
