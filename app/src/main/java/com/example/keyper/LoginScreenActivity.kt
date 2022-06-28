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
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor


class LoginScreenActivity : AppCompatActivity() {

    lateinit var biometricButton: Button
    lateinit var executor: Executor
    lateinit var biometricPrompt: BiometricPrompt
    lateinit var promptInfo: BiometricPrompt.PromptInfo

    @SuppressLint("PrivateResource")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()
        biometricButton = findViewById(R.id.biometricButton)
        val passwordInput = findViewById<EditText>(R.id.masterKeyTextView)
        val loginInfoTextView = findViewById<TextView>(R.id.loginInfoTextView)

        executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(this@LoginScreenActivity, executor, object: BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationError(errorCode: Int, errorString: CharSequence) {
                super.onAuthenticationError(errorCode, errorString)
                loginInfoTextView.text = errorString
                loginInfoTextView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_warning_24,
                    0,
                    0,
                    0
                )
                Handler().postDelayed({

                    loginInfoTextView.text = getString(R.string.empty_string)
                    loginInfoTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.empty, 0, 0, 0)
                }, 2000)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                loginInfoTextView.text = getString(R.string.successful_login)
                loginInfoTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_24, 0, 0, 0)
                Handler().postDelayed({
                    loginInfoTextView.text = getString(R.string.empty_string)
                    loginInfoTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.empty, 0, 0, 0)
                    val intent = Intent(baseContext, MainMenuActivity::class.java)
                    startActivity(intent)
                }, 1500)
            }
    })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric authentication")
            .setNegativeButtonText("Cancel")
            .build()

        biometricButton.setOnClickListener{
            biometricPrompt.authenticate(promptInfo)
        }

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
        val db = DBHandler(this)

        db.setMasterKey()

        validateButton.setOnClickListener {
            var passwordText = passwordInput.text.toString()
            val successfulLogin = db.equalsMasterKey(passwordText)

            if (TextUtils.isEmpty(passwordText)) {
                loginInfoTextView.text = getString(R.string.no_password)
                loginInfoTextView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_warning_24,
                    0,
                    0,
                    0
                )
            } else if (successfulLogin) {
                loginInfoTextView.text = getString(R.string.successful_login)
                loginInfoTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_24, 0, 0, 0)
                Handler().postDelayed({
                    loginInfoTextView.text = getString(R.string.empty_string)
                    loginInfoTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.empty, 0, 0, 0)
                    val intent = Intent(baseContext, MainMenuActivity::class.java)
                    startActivity(intent)
                }, 1500)
            } else {
                loginInfoTextView.text = getString(R.string.wrong_password)
                loginInfoTextView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_warning_24,
                    0,
                    0,
                    0
                )
                passwordInput.text.clear()
            }
            Handler().postDelayed({

                loginInfoTextView.text = getString(R.string.empty_string)
                loginInfoTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.empty, 0, 0, 0)
            }, 2000)
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