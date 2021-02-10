import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.FileContentReplacer
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.replaceContent
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.*
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
    description = "Teamcity Kotlin DSL Test on Local and Server"

    buildType(BuildTeamcity)
}

fun readScript(path: String): String {
    val bufferedReader: BufferedReader = File(path).bufferedReader()
    return bufferedReader.use { it.readText() }.trimIndent()
}


object BuildTeamcity : BuildType({
    name = "Build-Teamcity"

    params {
        param("system.MajorMinorVersion.Master", "1.1")
        param("system.MajorMinorVersion.Develop", "1.5")
    }

    buildNumberPattern = "%system.MajorMinorVersion.Master%.%build.counter%"


    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
//        powerShell {
//            name = "Poweshell Test"
//            id = "RUNNER_1"
//            workingDir = ".teamcity"
//            platform = PowerShellStep.Platform.x64
//            scriptMode = file {
//                path = "scripts/powershell.ps1"
//            }
//        }
        python {
            id = "RUNNER_3"
            name = "Python Test"
//            pythonVersion = customPython {
//                executable = """C:\Users\gaura\anaconda3\python"""
//            }
            command = file {
                filename = ".teamcity/scripts/version.py"
            }
        }
        script {
            id = "RUNNER_1"
            val number = "%system.MajorMinorVersion.Master%.%build.counter%"
            workingDir = ".teamcity"
            scriptContent= """
                echo "Build number is  $number"
                echo "$number" >> logfile.txt
                """.trimIndent()
        }

        gradle {
            id = "RUNNER_2"
            tasks = "printPro"
            gradleParams = "--info --stacktrace"
            gradleWrapperPath = ""
        }
        stepsOrder = arrayListOf("RUNNER_1", "RUNNER_3","RUNNER_2")
    }

    triggers {
        vcs {
            triggerRules = """
                -:*.md
                -:.gitignore
            """.trimIndent()
        }
    }

    features {
        commitStatusPublisher {
            vcsRootExtId = "${DslContext.settingsRoot.id}"
            publisher = github {
                githubUrl = "https://api.github.com"
                authType = personalToken {
                    token = "credentialsJSON:42715f5c-08f3-4c89-bde0-913f9b818614"
                }
            }
        }
//        replaceContent {
//            fileRules = "**/version.txt"
//            pattern = "VERSION_BUILD"
//            regexMode = FileContentReplacer.RegexMode.REGEX_MIXED
//            replacement = """%teamcity.agent.work.dir%\nd_r\bin\isf"""
//        }
    }
})
