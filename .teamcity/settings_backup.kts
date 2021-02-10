package _Self.buildTypes
import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import java.io.BufferedReader
import java.io.File
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
            scriptContent = readScript("scripts\\simple.sh")
        }
        stepsOrder = arrayListOf("script.from.file.1")
    }
})