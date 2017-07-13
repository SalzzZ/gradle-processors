Gradle Processors
=================

A plugin for Gradle that cleans up integration of Java 6+ [annotation processors][] with the
[Eclipse][], [IDEA][] and [FindBugs][] plugins.

[annotation processors]: http://docs.oracle.com/javase/6/docs/api/javax/annotation/processing/Processor.html
[Eclipse]: https://docs.gradle.org/current/userguide/eclipse_plugin.html
[IDEA]: https://docs.gradle.org/current/userguide/idea_plugin.html
[FindBugs]: https://docs.gradle.org/current/userguide/findbugs_plugin.html

[![GitHub release](https://img.shields.io/github/release/palantir/gradle-processors.svg?maxAge=60)](https://plugins.gradle.org/plugin/org.inferred.processors)
[![Travis](https://img.shields.io/travis/palantir/gradle-processors.svg?maxAge=60)](https://travis-ci.org/palantir/gradle-processors)
[![license](https://img.shields.io/github/license/palantir/gradle-processors.svg?maxAge=2592000)](http://www.apache.org/licenses/LICENSE-2.0)

Quickstart
----------

To use it, add the following to your projects' build.gradle file:

```gradle

plugins {
  id 'org.inferred.processors' version '<LATEST VERSION>'
}
```

You can now include annotation processors with the `processor` dependency type:

```gradle

dependencies {
  processor 'com.google.auto.value:auto-value:1.0'
}
```

The `eclipse` and `idea` tasks will now configure your IDE to run annotation processors as part
of their regular compilation. Alternatively, you can continue to open your build.gradle file
directly from within IntelliJ.

Gradle 2.0 and earlier
----------------------

For users of Gradle 2.0 and earlier, the `plugins` API is not available. Instead, add the
following to your top-level build.gradle file:

```gradle

buildscript {
  repositories {
    maven {
      url 'https://plugins.gradle.org/m2/'
    }
  }
  dependencies {
    classpath 'gradle.plugin.org.inferred:gradle-processors:<LATEST VERSION>'
  }
}
```

And just the apply directive to your subproject build.gradle files:

```gradle

apply plugin: 'org.inferred.processors'
```

You can now include annotation processors with the `processor` dependency type, as above.

Building from source
--------------------

To build the project from source, run `./gradlew build`, or `gradlew.bat build` on Windows,
in the root directory of your checkout. You will need Java installed.

Difference from upstream
-------
current IntelliJ idea sets the annotation processor configuration globally, while for multi-projects gradle build, this plugin need to be applied per-module, thus might be inefficient. Moreover, we build in-house processor. idea won't build if all projects use the same settings (use classpath to find processors). The processors module need different config (disable AP). So I'll just put idea configuration in a separate gradle file, applied only once, to the root build.gradle

License
-------

```
Copyright 2015 Palantir Technologies, Inc. All rights reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

