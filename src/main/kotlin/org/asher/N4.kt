package org.asher

import com.google.gson.Gson
import org.asher.calculateTOTP

fun main() {
    val password = "hello"
    val filename = "/Users/astern/Temporary/t.bin"
    val content = loadFile(filename, password)

    val gson = Gson()
    val siteToKey = gson.fromJson<Map<String, String>>(content, Map::class.java)
    println(siteToKey)

    for (site in siteToKey.keys) {
        val key = siteToKey[site]!!
        println("$site : ${calculateTOTP(key)}")
    }


}