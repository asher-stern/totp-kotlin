package org.asher

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File

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

fun addKey(password: String, filename: String, host: String, key: String) {
    val gson = GsonBuilder().setPrettyPrinting().create()
    val siteToKey = if (File(filename).exists()) {
        val content = loadFile(filename, password)
        gson.fromJson<LinkedHashMap<String, String>>(content, LinkedHashMap::class.java)
    }
    else {
        LinkedHashMap<String, String>()
    }
    siteToKey[host] = key
    val newContent = gson.toJson(siteToKey)
    saveContent(password, filename, newContent)
}

