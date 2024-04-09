import org.asher.addKey
import org.asher.saveContent
import org.asher.showCodes
import org.asher.showContent
import java.io.File
import kotlin.system.exitProcess


val HELP = """
    First argument: encrypted file name.
    
    Second argument:
    empty: show codes.
    save: [JSON file] encrypt the content given by the third argument.
    show: show encrypted content
    add: [host key] add the key for the host
    """.trimIndent()


fun verifyPassword(password: String) {
    println("Enter password again:")
    val passwordAgain = String(System.console().readPassword())
    if (passwordAgain != password) {
        println("Passwords not identical.")
        exitProcess(0)
    }
}

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
            verifyPassword(password)
            val content = File(contentFileName).readText()
            saveContent(password, filename, content)
        }
        "show" -> {
            showContent(password, filename)
        }
        "add" -> {
            if (args.size < 4) {
                println(HELP)
                exitProcess(0)
            }
            verifyPassword(password)
            addKey(password, filename, args[2], args[3])
        }
        else -> {
            println(HELP)
        }
    }
}
