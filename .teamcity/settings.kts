import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs
import java.io.BufferedReader
import java.io.File

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2020.2"

project {
    description = "TEamcity Kotlin DSL Test"

    buildType(BuildTeamcity)
}

fun readScript(path: String): String {
    val bufferedReader: BufferedReader = File(path).bufferedReader()
    return bufferedReader.use { it.readText() }.trimIndent()
}
object CommandLineRunnerTest : BuildType({
    name = "Command Line Runner Test"
    steps {
        script {
            name = "Imported from a file"
            id = "script.from.file.1"
            scriptContent = readScript("scripts\\test.sh")
        }
        stepsOrder = arrayListOf("script.from.file.1")
    }
})


object BuildTeamcity : BuildType({
    name = "Build-Teamcity"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        gradle {
            //tasks = "clean build"

            tasks = "printPro"
            gradleWrapperPath = ""
            gradleParams = "--info --stacktrace"
        }
    }

    triggers {
        vcs {
            triggerRules ="""
            -:*.md
            -:.gitignore
            """.trimIndent()
        }
    }

    features {
        commitStatusPublisher {
            publisher = github {
                githubUrl = "https://api.github.com"
                authType = personalToken {
                    token = "credentialsJSON:0daa7aae-da98-41c6-ab6f-bbbd9daefe83"
                }
            }
            param("github_oauth_user", "gauravcanon")
        }
    }
})
