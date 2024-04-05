package org.asher

fun n2() {
    println("hello. enter password.")
    val pass = System.console().readPassword()
    println(pass)
}

fun main() {
    n2()
}