import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.exec
object CommandLineRunnerTest : BuildType({
    name = "Command Line Runner Test"
    steps {
        exec {
            id = "RUNNER_16"
            path = "scripts/test.sh"
//            arguments = "param1 param2"
//            param("org.jfrog.artifactory.selectedDeployableServer.downloadSpecSource", "Job configuration")
//            param("org.jfrog.artifactory.selectedDeployableServer.useSpecs", "false")
//            param("org.jfrog.artifactory.selectedDeployableServer.uploadSpecSource", "Job configuration")
        }
        stepsOrder = arrayListOf("RUNNER_16")
    }
})