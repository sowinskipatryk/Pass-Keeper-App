package com.example.keyper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        val textView = findViewById<TextView>(R.id.textView)
        val imageView = findViewById<ImageView>(R.id.imageView)
        val handler = Handler()

        handler.postDelayed({
            textView.text = getString(R.string.app_name)
            imageView.visibility = VISIBLE
        }, 500)

        handler.postDelayed({
            val intent = Intent(this, LoginScreenActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}