import org.asher.saveContent
import org.asher.showCodes
import org.asher.showContent
import java.io.File
import kotlin.system.exitProcess


val HELP = """
    First argument: encrypted file name.
    
    Second argument:
    empty: show codes.
    save: encrypt the content given by the third argument.
    show: show encrypted content
    """.trimIndent()

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println(HELP)
        exitProcess(0)
    }
    val filename = args[0]
    val operation = if (args.size > 1) args[1] else null

    println("Enter password:")
    val password = String(System.console().readPassword())

    when (operation) {
        null -> {
            showCodes(password, filename)
        }
        "save" -> {
            val contentFileName = args[2]
            val content = File(contentFileName).readText()
            saveContent(password, filename, content)
        }
        "show" -> {
            showContent(password, filename)
        }
        else -> {
            println(HELP)
        }
    }
}