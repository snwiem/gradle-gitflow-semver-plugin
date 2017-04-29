# semflow

An very simple automated versioning plugin for Gradle based on
[semver2](http://semver.org/) and [gitflow](https://github.com/nvie/gitflow).

Currently the plugin supports a very basic semantic versioning scheme base on release modifier and
build meta information.

The release modifier depends on current repository gitflow status. The build meta information is just
used to identify uncommited repository statuses.



## Installation

```groovy
buildScript {
   dependencies {
     classpath group: 'org.syracus.gradle', name: 'semflow-plugin', version: '1.0.0-SNAPSHOT'
   }
}
```

## Usage

```groovy
apply plugin: 'semflow'

// configure (optional)
semflow {
  initialVersion = '0.0.0'      // default: 0.1.0
  alphaModifier = 'SNAPSHOT'    // default: ALPHA
  betaModifier = 'RC'           // default: BETA
  dirtyIdentifier = 'DIRTY'     // default: DIRTY
}

// set artefact version
version = semflow.version()
```

In this (default) implementation the assumption is that all artefacts build in one of the development branches
```develop```, ```feature```, ```bugfix``` and ```support``` are always **ALPHA** releases.

Artefacts build in ```release``` branch are always **BETA** releases and all artefacts build in 
```master``` or ```hotfix``` are always **RELEASES**.

When the local repository was uncommited (contained unstashed changes) the **DIRTY** identifier is 
appended to the build meta information automatically.

All **ALPHA** releases are always using the same version of the latest official release. This is the tag
created by ```$ git flow release finish```.
If no such tag can be found the ```initialVersion``` will be used.

Additionally the plugin supports the following tasks:

Task | Description
---|---
```projectVersion``` | Prints the current project version which will be used for artefact building
```repositoryVersion``` | Prints the latest repository version. This is the latest release
```nextVersion``` | Prints the next version which will be used by ```semflow.version()```.

### Examples

#### Development builds

Assume the latest official release was tagged with version ```1.5.0```. You are currently
working in the ```develop/``` branch of your gitflow repository. Currently no changes to the local
repository are uncommited (including untracked files) the generated version is

```1.5.0-ALPHA```

If you have changed something which has not been commited

```1.5.0-ALPHA+DIRTY```

will be used.

If you are developing a new feature and created a new feature branch ```feature/FEAT-543``` the feature
name will be used as modifier. So similar to the development build the versions would be

```1.5.0-FEAT-543``` and ```1.5.0-FEAT-543+DIRTY```.

Please notice that this requires that your feature names will match the modifier specification of 
[semver2](http://semver.org/) which is ```[0-9a-zA-Z-]```.

The same behaviour applies to bugfix branches!

#### Releasing and Hotfixes

If it comes to releases the version bumping needs to be done manually with gitflow. This is
because you need to give the release version when creating the release branch.
Here the provided tasks come in handy. Just either check the last release version using ```git repositoryVersion```
and decide based on the feature set of the new release which new release numbers need to be bumped.

For releases not breaking any public APIs usually just the minor number needs be bumped. The patch number
is reverted to 0.

```bash
$ ./gradlew repositoryVersion
:repositoryVersion
Repository version: 1.8.8

$ git flow release start 1.9.0
```

If you build your releases for testing the **BETA** modifier will be used. The plugin will
automatically use the version given for the release branch.

So a build in the release branch above will create an artifact with version

```1.9.0-BETA``` and ```1.9.0-BETA+DIRTY```

If your tests have been successfull and the release can be committed you usually to a 

```bash
$ git flow release finish
```

This merges all changes into master and development branch and creates a release version tag

```<prefix>-1.0.9```

The ```<prefix>``` is the prefix you've specified when initalizing the repository for gitflow.

Please notice that **semflow** will automatically parse the gitflow configuration to identify the
correct prefix to search for release tags.

In very rare cases you're forced to use the hotfix feature of gitflow to make important changes
directly on the master branch. The workflow is the same as for releases. The only difference is
that regarding to semver2 you should only bump the patch number of the release version.

So if you need to fix the latest release directly you should get the latest release version and
create your hotfix branch with the patch number bumped.

```bash
$ ./gradlew repositoryVersion
:repositoryVersion
Repository version: 1.9.0

$ git flow hotfix start 1.9.1
```




## Contributing

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

## History

TODO: Write history

## Credits

TODO: Write credits

## License

TODO: Write license