package com.example.networkencryption

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {


    var encrypt = NetworkCallTokenEncipher()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.view_text) as TextView
        textView.setOnClickListener{
            Toast.makeText(this@MainActivity, "token copied to clipboard", Toast.LENGTH_LONG).show()

            var clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            var clip = ClipData.newPlainText("token", encrypt.validateToken)
            clipboard.setPrimaryClip(clip)
        }
    }
}
