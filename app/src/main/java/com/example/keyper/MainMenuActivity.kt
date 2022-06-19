package com.example.keyper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val actionBar = supportActionBar
        actionBar!!.title = "Main Menu"
        supportActionBar?.setBackgroundDrawable(getDrawable(R.color.actionbar_color))
        actionBar.setDisplayHomeAsUpEnabled(false)

        val goToPassGeneratorScreenButton = findViewById<Button>(R.id.goToPassGeneratorScreenButton)
        val goToPassCheckScreenButton = findViewById<Button>(R.id.goToPassCheckScreenButton)
        val addServicePasswordButton = findViewById<Button>(R.id.addServicePasswordButton)
        val updateServicePasswordButton = findViewById<Button>(R.id.updateServicePasswordButton)
        val updateMasterkKeyButton = findViewById<Button>(R.id.updateMasterKeyButton)

        goToPassGeneratorScreenButton.setOnClickListener {
            val intent = Intent(this, PassGeneratorActivity::class.java)
            startActivity(intent)
        }
        goToPassCheckScreenButton.setOnClickListener {
            val intent = Intent(this, PassViewerActivity::class.java)
            startActivity(intent)
        }

        addServicePasswordButton.setOnClickListener {
            val intent = Intent(this, PassCreatorActivity::class.java)
            startActivity(intent)
        }

        updateServicePasswordButton.setOnClickListener {
            val intent = Intent(this, PassEditorActivity::class.java)
            startActivity(intent)
        }

        updateMasterkKeyButton.setOnClickListener {
            val intent = Intent(this, MasterKeyEditorActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

}