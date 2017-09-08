package brunodles.buildconfig

import org.gradle.api.Plugin
import org.gradle.api.Project

class BuildConfigPlugin implements Plugin<Project> {

    @SuppressWarnings("GroovyUnusedDeclaration")
    public static final String VERSION = "0.1"
    private BuildConfigExtension extension

    @Override
    void apply(Project project) {
        project.sourceSets {
            main.java.srcDir 'build/build-config'
        }
        def task = project.tasks.create("generateBuildConfigClasses", BuildConfigTask.class)
        def compileKotlin = project.tasks.getByName("compileKotlin")
        if (compileKotlin != null)
            compileKotlin.dependsOn.add(task)
        else
            project.tasks.getByName("compileJava").dependsOn.add(task)

        extension = project.extensions.create("buildconfig", BuildConfigExtension)
    }
}
