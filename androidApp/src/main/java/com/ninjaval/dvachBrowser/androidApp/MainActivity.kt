package com.ninjaval.dvachBrowser.androidApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.ninjaval.dvachBrowser.shared.Greeting
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.ninjaval.dvachBrowser.shared.Sdk
import com.ninjaval.dvachBrowser.shared.cache.DatabaseDriverFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var sdk: Sdk

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val text = Greeting().greeting()
        val tv: TextView = findViewById(R.id.text_view)
        tv.text = text

        val btn: Button = findViewById(R.id.greetBtn)
        btn.setOnClickListener { greet() }
        sdk = Sdk(DatabaseDriverFactory(this))
    }

    private fun greet() {
        CoroutineScope(IO).launch {
            val boards = sdk.getBoards()
            Log.d("APP", boards.toString())
        }
    }
}
