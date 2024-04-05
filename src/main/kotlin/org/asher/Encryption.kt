package org.asher


import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.security.SecureRandom
import java.util.Arrays
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec



fun generateKey(password: CharArray, salt: ByteArray, iterations: Int = 10000, keyLength: Int = 256): ByteArray {
    return try {
        val spec = PBEKeySpec(password, salt, iterations, keyLength)
        val skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val key = skf.generateSecret(spec)
        key.encoded
    } catch (e: NoSuchAlgorithmException) {
        throw RuntimeException("PBKDF2WithHmacSHA256 algorithm not available", e)
    } catch (e: InvalidKeySpecException) {
        throw RuntimeException("Invalid key specification", e)
    } finally {
        // Clear the password from memory
        Arrays.fill(password, '\u0000')
    }
}



fun encryptData(data: ByteArray, key: ByteArray, iv: ByteArray): ByteArray? {
    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    val secretKey = SecretKeySpec(key, "AES")
    val ivSpec = IvParameterSpec(iv)
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)
    return cipher.doFinal(data)
}

fun decryptData(encryptedData: ByteArray, key: ByteArray, iv: ByteArray): ByteArray? {
    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    val secretKey = SecretKeySpec(key, "AES")
    val ivSpec = IvParameterSpec(iv)
    cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
    return cipher.doFinal(encryptedData)
}


fun saveFile(content: String, filename: String, password: String) {
    val passwordCharArray = password.toCharArray()

    val salt = ByteArray(16)
    SecureRandom().nextBytes(salt)
    val key = generateKey(passwordCharArray, salt)

    val saltIv = ByteArray(16)
    SecureRandom().nextBytes(saltIv)
    val iv = generateKey(passwordCharArray, saltIv, keyLength = 128)

    val encryptedData = encryptData(content.toByteArray(), key, iv)!!

    ObjectOutputStream(FileOutputStream(filename)).use { stream ->
        stream.writeObject(salt)
        stream.writeObject(saltIv)
        stream.writeObject(encryptedData)
    }
}


fun loadFile(filename: String, password: String): String {
    val passwordCharArray = password.toCharArray()
    val decryptedData = ObjectInputStream(FileInputStream(filename)).use { stream ->
        val salt = stream.readObject() as ByteArray
        val saltIv = stream.readObject() as ByteArray
        val encryptedData = stream.readObject() as ByteArray

        val key = generateKey(passwordCharArray, salt)
        val iv = generateKey(passwordCharArray, saltIv, keyLength = 128)
        decryptData(encryptedData, key, iv)!!
    }
    return String(decryptedData)
}


//fun main() {
//    val password = "myPassword".toCharArray()
//    val salt = ByteArray(16)
//    SecureRandom().nextBytes(salt)
//    val key = generateKey(password, salt)
//
//    val saltIv = ByteArray(16)
//    SecureRandom().nextBytes(saltIv)
//    val iv = generateKey(password, saltIv, keyLength = 128)
//
//    val plaintext = "Hello, World!".toByteArray()
//    val encryptedData = encryptData(plaintext, key, iv)!!
//    val decryptedData = decryptData(encryptedData, key, iv)
//
//    println("Plaintext: ${String(plaintext)}")
//    println(encryptedData.joinToString ("" ) { "%02x".format(it) })
//    println("Decrypted: ${String(decryptedData!!)}")
//}
