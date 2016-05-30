package org.inferred.gradle

import groovy.text.SimpleTemplateEngine
import org.gradle.api.GradleException
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ResolvedConfiguration
import org.gradle.api.file.FileCollection
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.quality.FindBugs
import org.gradle.api.specs.Spec
import org.gradle.plugins.ide.eclipse.EclipsePlugin
import org.gradle.plugins.ide.idea.IdeaPlugin

class ProcessorsPlugin implements Plugin<Project> {

  void apply(Project project) {

    project.configurations.create('processor')

    project.extensions.create('processors', ProcessorsExtension)
    project.processors {
      // Used by Eclipse and IDEA
      sourceOutputDir = 'src/main/java-generated'

      // Used by IDEA (Eclipse does not compile test sources separately)
      testSourceOutputDir = 'src/test/java-generated'
    }

    /**** javac, groovy, etc. *********************************************************************/
    project.plugins.withType(JavaPlugin, { plugin ->
      project.sourceSets.each { it.compileClasspath += project.configurations.processor }
      project.compileJava.dependsOn project.task('processorPath', {
        doLast {
          String path = getProcessors(project).getAsPath()
          project.compileJava.options.compilerArgs += ["-processorpath", path]
        }
      })
      project.javadoc.dependsOn project.task('javadocProcessors', {
        doLast {
          Set<File> path = getProcessors(project).files
          project.javadoc.options.classpath += path
        }
      })
    })
  }

  /** Runs {@code action} on element {@code name} in {@code collection} whenever it is added. */
  private static <T> void withName(
      NamedDomainObjectCollection<T> collection, String name, Closure action) {
    T object = collection.findByName(name)
    if (object != null) {
      action.call(object)
    } else {
      collection.whenObjectAdded { o ->
        String oName = collection.getNamer().determineName(o)
        if (oName == name) {
          action.call(o)
        }
      }
    }
  }

  static FileCollection getProcessors(Project project) {
    ResolvedConfiguration config = project.configurations.processor.resolvedConfiguration
    return project.files(config.getFiles({ d -> true } as Spec<Object>))
  }

}

class ProcessorsExtension {
  String sourceOutputDir
  String testSourceOutputDir
}
