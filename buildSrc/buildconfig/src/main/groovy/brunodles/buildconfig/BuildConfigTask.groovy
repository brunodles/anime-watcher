package brunodles.buildconfig

import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs

class BuildConfigTask extends SourceTask {

    String group = "buildConfig"
    String description = "Generate BuildConfig class."

    BuildConfigTask() {
        outputs.upToDateWhen { false }
        source("src")
        include("**${File.separatorChar}buildconfig.properties")
        finalizedBy("assemble")
    }

    @TaskAction
    def execute(IncrementalTaskInputs inputs) {

        def buildDir = new File(project.getBuildDir(), "build-config")
        buildDir.mkdirs()

        def outFile = new File(buildDir, "BuildConfig.java")
        outFile.createNewFile()
        StringBuilder content = new StringBuilder("public final class BuildConfig {\n")

        project.buildconfig.properties.each { f ->
            content.append("\tpublic static final String ${f.key.toUpperCase()} = \"${f.value}\";\n")
        }

        getInputs().files.forEach { file ->
            println "BuildConfig file -> ${file.getAbsolutePath()}"
            def properties = new Properties()
            if (file.exists())
                properties.load(file.newReader())

            properties.each { property ->
                content.append("\tpublic static final String ${property.key.toUpperCase()} = \"${property.value}\";\n")
            }

        }
        content.append("}\n")
        outFile.write(content.toString())
    }

}