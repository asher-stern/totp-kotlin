package org.asher

import com.google.gson.Gson
import com.google.gson.GsonBuilder

fun main() {
    val m1 = mapOf("a" to "b", "c" to "d")
    println(m1)
    val gson = GsonBuilder().setPrettyPrinting().create()
    // val gson = Gson()
    val m1Str = gson.toJson(m1)
    print(m1Str)

}