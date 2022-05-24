package com.example.keyper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ChoiceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choice)

        val actionBar = supportActionBar
        actionBar!!.title = "Main Menu"
        actionBar.setDisplayHomeAsUpEnabled(false)

        val goToPassGeneratorScreenButton = findViewById<Button>(R.id.goToPassGeneratorScreenButton)
        val goToPassCheckScreenButton = findViewById<Button>(R.id.goToPassCheckScreenButton)
        val addServicePasswordButton = findViewById<Button>(R.id.addServicePasswordButton)

        goToPassGeneratorScreenButton.setOnClickListener {
            val intent = Intent(this, PassGeneratorActivity::class.java)
            startActivity(intent)
        }
        goToPassCheckScreenButton.setOnClickListener {
            val intent = Intent(this, PassCheckActivity::class.java)
            startActivity(intent)
        }

        addServicePasswordButton.setOnClickListener {
            val intent = Intent(this, PassSaveActivity::class.java)
            startActivity(intent)
        }
    }
}