package org.asher

import com.google.gson.Gson

fun showCodes(password: String, filename: String) {
    val content = loadFile(filename, password)

    val gson = Gson()
    val siteToKey = gson.fromJson<Map<String, String>>(content, Map::class.java)

    for (site in siteToKey.keys) {
        val key = siteToKey[site]!!
        println("$site : ${calculateTOTP(key)}")
    }
}

fun showContent(password: String, filename: String) {
    val content = loadFile(filename, password)
    println(content)
}

fun saveContent(password: String, filename: String, content: String) {
    saveFile(content, filename, password)
}

