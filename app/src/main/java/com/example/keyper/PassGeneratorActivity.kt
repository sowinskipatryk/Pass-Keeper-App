package com.example.keyper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import java.lang.StringBuilder
import java.security.SecureRandom
import java.util.*

class PassGeneratorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pass_generator)

        val actionBar = supportActionBar
        actionBar!!.title = "Pass Generator"
        actionBar.setDisplayHomeAsUpEnabled(true)

        val generatePassButton = findViewById<Button>(R.id.generatePassButton)
        val generatedPassTextView = findViewById<TextView>(R.id.generatedPassTextView)
        val saveButton = findViewById<Button>(R.id.saveButton)

        generatePassButton.setOnClickListener{

            val specialChars = "!@#$%^&*()"
            val lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz"
            val upperCaseLetters = lowerCaseLetters.uppercase()

            val digitsNum = SecureRandom().nextInt(3) + 1
            val specialCharsNum = SecureRandom().nextInt(3) + 1
            val upperCaseLettersNum = SecureRandom().nextInt(3) + 1
            val lowerCaseLettersNum = 10-digitsNum-specialCharsNum-upperCaseLettersNum
            val password = StringBuilder(10)

            for (x in 0 until upperCaseLettersNum) {
                val pickedLetterIndex = (upperCaseLetters.indices).random()
                password.append(upperCaseLetters[pickedLetterIndex])
            }

            for (x in 0 until specialCharsNum) {
                val specialCharIndex = (specialChars.indices).random()
                password.append(specialChars[specialCharIndex])
            }

            for (x in 0 until digitsNum) {
                password.append(SecureRandom().nextInt(10))
            }

            for (x in 0 until lowerCaseLettersNum) {
                val pickedLetterIndex = (lowerCaseLetters.indices).random()
                password.append(lowerCaseLetters[pickedLetterIndex])
            }

            val shuffledPasswordList= password.toMutableList().shuffled()
            var shuffledPasswordString = ""

            for (char in shuffledPasswordList){
                shuffledPasswordString += char
            }

            generatedPassTextView.text = shuffledPasswordString

            }

        saveButton.setOnClickListener {
            val intent = Intent(this, PassSaveActivity::class.java)
            startActivity(intent)
        }

        }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)

    }

}