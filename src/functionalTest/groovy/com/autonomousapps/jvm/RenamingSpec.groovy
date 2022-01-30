package com.autonomousapps.jvm

import com.autonomousapps.jvm.projects.RenamingProject
import org.spockframework.runtime.extension.builtin.PreconditionContext
import spock.lang.IgnoreIf

import static com.autonomousapps.utils.Runner.build
import static com.google.common.truth.Truth.assertThat

final class RenamingSpec extends AbstractJvmSpec {

  // TODO V2: support has not yet been added to v2
  @IgnoreIf({ PreconditionContext it -> it.sys.'dependency.analysis.old.model' == 'false' })
  def "dependencies are renamed when renamer is used (#gradleVersion)"() {
    given:
    def project = new RenamingProject()
    gradleProject = project.gradleProject

    when:
    build(gradleVersion, gradleProject.rootDir, 'buildHealth')

    then:
    assertThat(actualAdviceConsole()).contains(project.expectedRenamedConsoleReport())

    where:
    gradleVersion << gradleVersions()
  }
}
