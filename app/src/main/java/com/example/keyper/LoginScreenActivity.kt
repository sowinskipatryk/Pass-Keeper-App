package com.example.keyper

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity


class LoginScreenActivity : AppCompatActivity() {

    @SuppressLint("PrivateResource")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val actionBar = supportActionBar
        actionBar!!.title = "Sign in"
        supportActionBar?.setBackgroundDrawable(getDrawable(R.color.actionbar_color))
        actionBar.setDisplayHomeAsUpEnabled(false)

        val passwordInput = findViewById<EditText>(R.id.masterKeyTextView)

        passwordInput.setOnKeyListener(View.OnKeyListener{ v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && passwordInput.isFocused) {
                this.currentFocus?.let { view ->
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(view.windowToken, 0)
                }
                passwordInput.clearFocus()
                return@OnKeyListener true
            }
            false
        })

        val validateButton = findViewById<Button>(R.id.loginButton)
        val validationInfoTextView = findViewById<TextView>(R.id.loginInfoTextView)
        val db = DBHandler(this)

        db.setMasterKey()

        validateButton.setOnClickListener {
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

    override fun onBackPressed() {
        val passwordInput = findViewById<EditText>(R.id.masterKeyTextView)
        if (passwordInput.isFocused) {
            passwordInput.clearFocus()
        } else {
            super.onBackPressed()
        }
    }



}