package com.example.keyper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val actionBar = supportActionBar

        actionBar!!.title = "Login"
        actionBar.setDisplayHomeAsUpEnabled(true)

        val validateButton = findViewById<Button>(R.id.validateButton)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val validationInfoTextView = findViewById<TextView>(R.id.validationInfoTextView)

        validateButton.setOnClickListener {
            if (TextUtils.isEmpty(passwordInput.getText().toString())) {
                validationInfoTextView.text = "Enter password!"
            }
            else if (passwordInput.getText().toString() == "1234") {
                val intent = Intent(this, ChoiceActivity::class.java)
                startActivity(intent)
            }
            else {
                validationInfoTextView.text = "Wrong password!"
            }

        }


    }
}