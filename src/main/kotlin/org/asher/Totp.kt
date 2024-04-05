package org.asher

import org.apache.commons.codec.binary.Base32
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.math.pow

private const val DIGITS = 6
private const val PERIOD = 30L // 30 seconds

fun calculateTOTP(base32Secret: String, timeIndex: Long = System.currentTimeMillis() / 1000 / PERIOD): String {
    val key = Base32().decode(base32Secret.replace(" ", "").uppercase())
    val data = counterToDataByteArray(timeIndex)

    val hash = hMacSha(key, data)
    return getDigitsFromHash(hash)
}

fun counterToDataByteArray(counter: Long): ByteArray {
    val data = ByteArray(8)
    var value = counter
    var i = 8
    while (i-- > 0) {
        data[i] = value.toByte()
        value = value ushr 8
    }
    return data
}

@Throws(NoSuchAlgorithmException::class, InvalidKeyException::class)
private fun hMacSha(key: ByteArray, data: ByteArray): ByteArray {
    val mac = Mac.getInstance("HmacSHA1")
    mac.init(SecretKeySpec(key, "HmacSHA1"))
    return mac.doFinal(data)
}

fun getDigitsFromHash(hash: ByteArray): String {
    val offset = hash[hash.size - 1].toInt() and 0xF
    var truncatedHash: Long = 0
    for (i in 0..3) {
        truncatedHash = truncatedHash shl 8
        truncatedHash = truncatedHash or (hash[offset + i].toInt() and 0xFF).toLong()
    }
    truncatedHash = truncatedHash and 0x7FFFFFFFL
    truncatedHash %= 10.0.pow(DIGITS.toDouble()).toLong()

    // Left pad with 0s for an n-digit code
    return java.lang.String.format("%0$DIGITS" + "d", truncatedHash)
}

//fun main() {
//    print(calculateTOTP("HVR4CFHAFOWFGGFAGSA5JVTIMMPG6GMT"))
//}