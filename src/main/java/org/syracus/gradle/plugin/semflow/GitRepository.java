package org.syracus.gradle.plugin.semflow;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.gradle.api.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by snwiem on 4/28/2017.
 */
public class GitRepository {

    private static final Logger log = LoggerFactory.getLogger(GitRepository.class);

    private static final String REF_HEAD = "HEAD";

    // TODO: make this configurable in the final plugin
    private static final String MODIFIER_ALPHA = "ALPHA";
    private static final String MODIFIER_BETA = "BETA";
    private static final String DEFAULT_VERSION = "0.0.0";



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

    public static Repository getRepository(Project project) throws IOException {
        File projectDirectory = project.getProjectDir().getAbsoluteFile();
        return getRepository(projectDirectory);
    }

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

    public static Version getRepositoryVersion(final Repository repository, final SemflowExtension extension) {
        final GitFlowConfig flowConfig = new GitFlowConfig.Builder().fromExtension(extension).build();
        IVersionFactory versionFactory = new DefaultVersionFactory.Builder(flowConfig).extension(extension).build();

        Version repositoryVersion = versionFactory.getCurrentVersion(repository);
        return repositoryVersion;
    }

    public static Version getNextRepositoryVersion(final Repository repository, final SemflowExtension extension) {
        final GitFlowConfig flowConfig = new GitFlowConfig.Builder().fromExtension(extension).build();
        IVersionFactory versionFactory = new DefaultVersionFactory.Builder(flowConfig).extension(extension).build();

        Version repositoryVersion = versionFactory.getNextVersion(repository);
        return repositoryVersion;
    }

    public static boolean isClean(Repository repository) throws GitAPIException {
        Git git = new Git(repository);
        Status status = git.status().call();
        return status.isClean();
    }

    public static Branch getBranch(Repository repository) throws IOException, GitAPIException {
        String branchName = repository.getBranch();
        return new Branch(branchName, isClean(repository));
    }

    public static ObjectId getHead(Repository repository) {
        Ref headRef = repository.getAllRefs().get(REF_HEAD);
        if (null == headRef)
            throw new RepositoryException("No HEAD ref found in repository.");
        return headRef.getObjectId();
    }

    public static Map<ObjectId, Set<Tag>> getAllTags(Repository repository) {
        Map<ObjectId, Set<Tag>> map = new HashMap<>();
        Map<String,Ref> refs = repository.getTags();
        for (Map.Entry<String, Ref> ref : refs.entrySet()) {
            ObjectId objectId = getIdForRef(repository, ref.getValue());
            if (map.containsKey(objectId)) {
                map.get(objectId).add(Tag.withName(ref.getKey()));
            } else {
                Set<Tag> tags = new HashSet<>();
                tags.add(Tag.withName(ref.getKey()));
                map.put(objectId, tags);
            }
        }
        return map;
    }

    public static Map<ObjectId, Set<Tag>> getAllTagsWithPrefix(Repository repository, String prefix) {
        final Map<ObjectId, Set<Tag>> allTags = getAllTags(repository);
        return filterTagsWithPrefix(allTags, prefix);
    }

    public static Map<ObjectId, Set<Tag>> getAllTagsWithRegex(Repository repository, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return getAllTagsWithRegex(repository, pattern);
    }

    public static Map<ObjectId, Set<Tag>> getAllTagsWithRegex(Repository repository, Pattern regex) {
        final Map<ObjectId, Set<Tag>> allTags = getAllTags(repository);
        return filterTagsWithRegex(allTags, regex);
    }

    private static Map<ObjectId, Set<Tag>> filterTagsWithPrefix(final Map<ObjectId, Set<Tag>> allTags, final String prefix) {
        if (null != prefix) {
            return filterTagsWithPredicate(allTags, t -> {
                return t.getName().startsWith(prefix);
            });
        }
        return new HashMap<>();
    }

    private static Map<ObjectId, Set<Tag>> filterTagsWithRegex(final Map<ObjectId, Set<Tag>> allTags, final Pattern pattern) {
        if (null != pattern) {
            return filterTagsWithPredicate(allTags, t -> {
                Matcher matcher = pattern.matcher(t.getName());
                return matcher.matches();
            });
        }
        return new HashMap<>();
    }

    private static Map<ObjectId, Set<Tag>> filterTagsWithPredicate(final Map<ObjectId, Set<Tag>> allTags, Function<Tag, Boolean> predicate) {
        Map<ObjectId, Set<Tag>> allTagsWithPrefix = new HashMap<>();
        for (Map.Entry<ObjectId, Set<Tag>> entry : allTags.entrySet()) {
            Set<Tag> tagsWithPrefix = new HashSet<>();
            for (Tag tag : entry.getValue()) {
                if (predicate.apply(tag)) {
                    tagsWithPrefix.add(tag);
                }
            }
            if (0 != tagsWithPrefix.size()) {
                allTagsWithPrefix.put(entry.getKey(), tagsWithPrefix);
            }
        }
        return allTagsWithPrefix;
    }

    public static String stripPrefix(final Tag tag, final String prefix) {
        return tag.getName().startsWith(prefix) ? tag.getName().substring(prefix.length()) : tag.getName();
    }

    public static String stripRegex(final Tag tag, final String regex, final int group) {
        Pattern pattern = Pattern.compile(regex);
        return stripRegex(tag, pattern, group);
    }

    public static String stripRegex(final Tag tag, final Pattern regex, final int group) {
        Matcher matcher = regex.matcher(tag.getName());
        if (matcher.matches()) {
            return matcher.group(group);
        }
        return tag.getName();
    }

    public static Version findLatestTagVersionWithPrefix(Repository repository, Map<ObjectId, Set<Tag>> tags, String prefix)
        throws IOException
    {
        RevWalk walk = new RevWalk(repository);
        ObjectId headRef = GitRepository.getHead(repository);
        RevCommit headCommit = walk.parseCommit(headRef);
        walk.markStart(headCommit);
        // walks backwards in history
        for (RevCommit commit : walk) {
            ObjectId commitId = commit.getId();
            if (tags.containsKey(commitId)) {
                List<Version> foundVersions = new LinkedList<>();
                for (Tag tag : tags.get(commitId)) {
                    String versionString = null == prefix ? tag.getName() : GitRepository.stripPrefix(tag, prefix);
                    Version version = Version.fromString(versionString);
                    foundVersions.add(version);
                }
                if (0 < foundVersions.size()) {
                    Collections.sort(foundVersions);
                    return foundVersions.get(foundVersions.size()-1);
                }

            }
        }
        //return Version.fromString(DEFAULT_VERSION);;
        return null;
    }


    private static ObjectId getIdForRef(Repository repository, Ref ref) {
        Ref peeledRef = repository.peel(ref);
        ObjectId objectId = null;
        if (null == peeledRef.getPeeledObjectId()) {
            objectId = peeledRef.getObjectId();
        } else {
            objectId = peeledRef.getPeeledObjectId();
        }
        return objectId;
    }

    public static class RepositoryException extends RuntimeException {
        private static final long serialVersionUID = -1L;

        public RepositoryException() {
        }

        public RepositoryException(String message) {
            super(message);
        }

        public RepositoryException(String message, Throwable cause) {
            super(message, cause);
        }

        public RepositoryException(Throwable cause) {
            super(cause);
        }
    }


}
