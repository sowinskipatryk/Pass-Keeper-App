package com.example.keyper

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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

        passwordInput.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (!hasFocus) {
                    passwordInput.clearFocus()
                }
            }
        })

        val validateButton = findViewById<Button>(R.id.loginButton)
        val validationInfoTextView = findViewById<TextView>(R.id.loginInfoTextView)
        val db = DBHandler(this)

        db.setMasterKey()

        validateButton.setOnClickListener {
            var passwordText = passwordInput.text.toString()
            val successfulLogin = db.equalsMasterKey(passwordText)

            if (TextUtils.isEmpty(passwordText)) {
                validationInfoTextView.text = getString(R.string.no_password)
                validationInfoTextView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_warning_24,
                    0,
                    0,
                    0
                )
            } else if (successfulLogin) {
                validationInfoTextView.text = getString(R.string.empty_string)
                validationInfoTextView.setCompoundDrawablesWithIntrinsicBounds(
                    com.google.android.material.R.drawable.navigation_empty_icon,
                    0,
                    0,
                    0
                )
                val intent = Intent(this, MainMenuActivity::class.java)
                startActivity(intent)
            } else {
                validationInfoTextView.text = getString(R.string.wrong_password)
                validationInfoTextView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_warning_24,
                    0,
                    0,
                    0
                )
                passwordInput.text.clear()
            }
            Handler().postDelayed({

                validationInfoTextView.text = getString(R.string.empty_string)
                validationInfoTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.empty, 0, 0, 0)
            }, 2500)
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

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

}