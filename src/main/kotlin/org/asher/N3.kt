package org.asher

import org.asher.saveFile
import org.asher.loadFile


fun main() {
    val password = "hello"
    val filename = "/Users/astern/Temporary/t.bin"
    val content = """
        {
            nisuy: JBSWY3DPEHPK3PXP
        }
    """.trimIndent()
    saveFile(content, filename, password)
    val restored = loadFile(filename, password)
    println(restored)
}