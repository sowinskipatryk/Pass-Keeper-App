package com.example.keyper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ChoiceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choice)

        val goToPassGeneratorScreenButton = findViewById<Button>(R.id.goToPassGeneratorScreenButton)
        val goToPassCheckScreenButton = findViewById<Button>(R.id.goToPassCheckScreenButton)

        goToPassGeneratorScreenButton.setOnClickListener {
            val intent = Intent(this, PassGeneratorActivity::class.java)
            startActivity(intent)
        }
        goToPassCheckScreenButton.setOnClickListener {
            val intent = Intent(this, PassCheckActivity::class.java)
            startActivity(intent)
        }
    }
}