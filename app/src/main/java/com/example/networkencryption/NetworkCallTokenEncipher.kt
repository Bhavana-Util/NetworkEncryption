package com.example.networkencryption

import android.util.Base64
import java.security.SecureRandom
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class NetworkCallTokenEncipher {

    private val ENCRYPT_ALGO = "AES/CBC/PKCS7Padding"
    private val SECRET_KEY_FACTORY = "PBKDF2WithHmacSHA1"
    private val AES_IV_BIT = 128
    private val SALT_LENGTH_BYTE = 16
    private val AES_KEY_BIT = 256
    private val ITERATION_COUNT = 1324
    private val ALGORITHM = "AES"
    private val CHARSET = "UTF-8"
    private val random = SecureRandom()

    private fun encodeBase64(byteArray: ByteArray): String = Base64.encodeToString(byteArray, Base64.DEFAULT)

    private fun encrypt2(textToCipher: String, cipherKeyStore: String, cipherIVStore:String): String {

        val salt = ByteArray(SALT_LENGTH_BYTE)
        random.nextBytes(salt)

        //key generation
        val cipherTextByteArray: ByteArray = textToCipher.toByteArray(charset(CHARSET))
        val pbKeySpec = PBEKeySpec(cipherKeyStore.toCharArray(), salt, ITERATION_COUNT, AES_KEY_BIT)
        val secretKeyFactory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY)
        val keyBytes = secretKeyFactory.generateSecret(pbKeySpec).encoded
        val keySpec = SecretKeySpec(keyBytes, ALGORITHM)

        //iv generation
        val pbIVSpec = PBEKeySpec(cipherIVStore.toCharArray(), salt, ITERATION_COUNT, AES_IV_BIT)
        val ivBytes = secretKeyFactory.generateSecret(pbIVSpec).encoded
        val ivSpec = IvParameterSpec(ivBytes)

        //final Encryption
        val cipher = Cipher.getInstance(ENCRYPT_ALGO)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
        val encryptedCipherText = cipher.doFinal(cipherTextByteArray)

        return (encodeBase64(salt) + "_" + encodeBase64(encryptedCipherText))
    }

    var validateToken : String = encrypt2("VivekYouAreAwsome"+getTimestampGMT(),"PasswordKey","ivSecretPassword")

    private fun getTimestampGMT(): String {
        var df: DateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        df.timeZone = TimeZone.getTimeZone("GMT")
        var date = Date()
        return df.format(date)
    }
}

