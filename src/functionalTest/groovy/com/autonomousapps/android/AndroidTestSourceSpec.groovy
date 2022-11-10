package com.autonomousapps.android

import com.autonomousapps.android.projects.AndroidTestSourceProject
import org.gradle.util.GradleVersion

import static com.autonomousapps.advice.truth.BuildHealthSubject.buildHealth
import static com.autonomousapps.utils.Runner.build
import static com.google.common.truth.Truth.assertAbout

final class AndroidTestSourceSpec extends AbstractAndroidSpec {

  def "androidTest dependencies should be on androidTestImplementation (#gradleVersion AGP #agpVersion)"() {
    given:
    def project = new AndroidTestSourceProject(agpVersion as String)
    gradleProject = project.gradleProject

    when:
    build(gradleVersion as GradleVersion, gradleProject.rootDir, 'buildHealth')

    then:
    assertAbout(buildHealth())
      .that(project.actualBuildHealth())
      .isEquivalentIgnoringModuleAdvice(project.expectedBuildHealth)

    where:
    [gradleVersion, agpVersion] << gradleAgpMatrix()
  }

  // https://github.com/autonomousapps/dependency-analysis-android-gradle-plugin/issues/411
  def "kapt is used for android tests (#gradleVersion AGP #agpVersion)"() {
    given:
    def project = new AndroidTestSourceProject(agpVersion as String, true)
    gradleProject = project.gradleProject

    when:
    build(gradleVersion as GradleVersion, gradleProject.rootDir, 'buildHealth')

    then:
    assertAbout(buildHealth())
      .that(project.actualBuildHealth())
      .isEquivalentIgnoringModuleAdvice(project.expectedBuildHealth)

    where:
    [gradleVersion, agpVersion] << gradleAgpMatrix()
  }
}
