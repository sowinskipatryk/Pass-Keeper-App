package com.example.keyper

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class LoginScreenActivity : AppCompatActivity() {

    @SuppressLint("PrivateResource")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val actionBar = supportActionBar
        actionBar!!.title = "Sign in"
        actionBar.setDisplayHomeAsUpEnabled(false)

        val validateButton = findViewById<Button>(R.id.loginButton)
        val validationInfoTextView = findViewById<TextView>(R.id.loginInfoTextView)
        val db = DBHandler(this)

        db.setMasterKey()

        validateButton.setOnClickListener {
            val passwordInput = findViewById<EditText>(R.id.masterKeyTextView)
            val passwordText = passwordInput.text.toString()
            val successfulLogin = db.equalsMasterKey(passwordText)

            if (TextUtils.isEmpty(passwordText)) {
                validationInfoTextView.text = " Enter password!"
                validationInfoTextView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_warning_24,
                    0,
                    0,
                    0
                )
            } else if (successfulLogin) {
                validationInfoTextView.text = ""
                validationInfoTextView.setCompoundDrawablesWithIntrinsicBounds(
                    com.google.android.material.R.drawable.navigation_empty_icon,
                    0,
                    0,
                    0
                )
                val intent = Intent(this, MainMenuActivity::class.java)
                startActivity(intent)
            } else {
                validationInfoTextView.text = " Wrong password!"
                validationInfoTextView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_warning_24,
                    0,
                    0,
                    0
                )
            }

        }

    }
}