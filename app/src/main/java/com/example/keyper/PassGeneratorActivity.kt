package com.example.keyper

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import java.security.SecureRandom


class PassGeneratorActivity : AppCompatActivity() {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generator)

        val actionBar = supportActionBar
        actionBar!!.title = "Pass Generator"
        supportActionBar?.setBackgroundDrawable(getDrawable(R.color.actionbar_color))
        actionBar.setDisplayHomeAsUpEnabled(true)

        val generatePassButton = findViewById<Button>(R.id.generatePassButton)
        val generatedPassTextView = findViewById<TextView>(R.id.generatedPassTextView)
        val generatePassInfoTextView = findViewById<TextView>(R.id.generatePassInfoTextView)
        val saveButton = findViewById<Button>(R.id.saveButton)
        val uppercaseLettersSwitch = findViewById<SwitchCompat>(R.id.uppercaseLettersSwitch)
        val digitsSwitch = findViewById<SwitchCompat>(R.id.digitsSwitch)
        val specialCharsSwitch = findViewById<SwitchCompat>(R.id.specialCharsSwitch)
        val charNumSeekBar = findViewById<SeekBar>(R.id.charNumSeekBar)
        val charsNumTextView = findViewById<TextView>(R.id.charsNumTextView)

        charsNumTextView.text = (6 + charNumSeekBar.progress).toString()

        charNumSeekBar?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                charsNumTextView.text = (6 + charNumSeekBar.progress).toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        generatePassButton.setOnClickListener {

            val charNum = 6 + charNumSeekBar.progress
            val specialChars = "!@#$%^&*()"
            val lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz"
            val upperCaseLetters = lowerCaseLetters.uppercase()

            var upperCaseLettersNum = 0
            var digitsNum = 0
            var specialCharsNum = 0

            if (uppercaseLettersSwitch.isChecked) {
                upperCaseLettersNum = SecureRandom().nextInt(3) + 1
            }

            if (digitsSwitch.isChecked) {
                digitsNum = SecureRandom().nextInt(3) + 1
            }

            if (specialCharsSwitch.isChecked) {
                specialCharsNum = SecureRandom().nextInt(3) + 1
            }

            val lowerCaseLettersNum = charNum - digitsNum - specialCharsNum - upperCaseLettersNum
            val password = StringBuilder(charNum)

            for (x in 0 until upperCaseLettersNum) {
                val pickedLetterIndex = (upperCaseLetters.indices).random()
                password.append(upperCaseLetters[pickedLetterIndex])
            }

            for (x in 0 until specialCharsNum) {
                val specialCharIndex = (specialChars.indices).random()
                password.append(specialChars[specialCharIndex])
            }

            for (x in 0 until digitsNum) {
                password.append(SecureRandom().nextInt(charNum))
            }

            for (x in 0 until lowerCaseLettersNum) {
                val pickedLetterIndex = (lowerCaseLetters.indices).random()
                password.append(lowerCaseLetters[pickedLetterIndex])
            }

            val shuffledPasswordList = password.toMutableList().shuffled()
            var shuffledPasswordString = ""

            for (char in shuffledPasswordList) {
                shuffledPasswordString += char
            }

            generatedPassTextView.text = shuffledPasswordString

        }

        saveButton.setOnClickListener {
            if (generatedPassTextView.text.toString().equals("Tap button")) {
                generatePassInfoTextView.text = " Generate password first!"
                generatePassInfoTextView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_warning_24,
                    0,
                    0,
                    0
                )
            } else {
                generatePassInfoTextView.text = ""
                generatePassInfoTextView.setCompoundDrawablesWithIntrinsicBounds(
                    com.google.android.material.R.drawable.navigation_empty_icon,
                    0,
                    0,
                    0
                )
                val intent = Intent(this, PassCreatorActivity::class.java)
                intent.putExtra("passw", generatedPassTextView.text.toString())
                startActivity(intent)
            }
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        return true;
    }

}