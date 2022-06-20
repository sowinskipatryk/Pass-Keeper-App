package com.example.keyper

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class PassCreatorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creator)

        val actionBar = supportActionBar
        actionBar!!.title = "Pass Creator"
        supportActionBar?.setBackgroundDrawable(getDrawable(R.color.actionbar_color))
        actionBar.setDisplayHomeAsUpEnabled(true)

        val serviceNameTextView = findViewById<TextView>(R.id.serviceNameTextView)
        val servicePasswordTextView = findViewById<TextView>(R.id.servicePasswordTextView)
        val savePasswordButton = findViewById<Button>(R.id.savePasswordButton)
        val savingInfoTextView = findViewById<TextView>(R.id.savingInfoTextView)
        val db = DBHandler(this)
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            servicePasswordTextView.text = bundle.getString("passw")
        }

        serviceNameTextView.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && serviceNameTextView.isFocused && event.action == KeyEvent.ACTION_UP) {
                serviceNameTextView.clearFocus()
                servicePasswordTextView.requestFocus()
                return@OnKeyListener true
            }
            false
        })

        serviceNameTextView.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (!hasFocus) {
                    serviceNameTextView.clearFocus()
                }
            }
        })

        servicePasswordTextView.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && servicePasswordTextView.isFocused && event.action == KeyEvent.ACTION_UP) {
                this.currentFocus?.let { view ->
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(view.windowToken, 0)
                }
                servicePasswordTextView.clearFocus()
                return@OnKeyListener true
            }
            false
        })

        servicePasswordTextView.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (!hasFocus) {
                    servicePasswordTextView.clearFocus()
                }
            }
        })

        savePasswordButton.setOnClickListener {
            var serviceNameText = serviceNameTextView.text.toString()
            serviceNameText = serviceNameText.lowercase().capitalize()
            var servicePasswordText = servicePasswordTextView.text.toString()
            if ((serviceNameText.isEmpty()) || (servicePasswordText.isEmpty())) {
                savingInfoTextView.text = getString(R.string.missing_fields_error)
                savingInfoTextView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_warning_24, 0, 0, 0
                )
            } else {
                val successfulInsertion =
                    db.insertServicePassword(serviceNameText, servicePasswordText)
                if (successfulInsertion == true) {
                    savingInfoTextView.text = getString(R.string.save_successful)
                    savingInfoTextView.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_baseline_bookmark_added_24, 0, 0, 0
                    )
                    Handler().postDelayed({
                        serviceNameTextView.text = getString(R.string.empty_string)
                        servicePasswordTextView.text = getString(R.string.empty_string)
                    }, 2500)
                } else {
                    savingInfoTextView.text = getString(R.string.save_error)
                    savingInfoTextView.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_baseline_warning_24, 0, 0, 0
                    )
                }
            }
            Handler().postDelayed({
                savingInfoTextView.text = getString(R.string.empty_string)
                savingInfoTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.empty, 0, 0, 0)
            }, 2500)
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

    override fun onBackPressed() {
        val serviceNameTextView = findViewById<EditText>(R.id.serviceNameTextView)
        val servicePasswordTextView = findViewById<EditText>(R.id.servicePasswordTextView)
        if (serviceNameTextView.isFocused) {
            serviceNameTextView.clearFocus()
        } else if (servicePasswordTextView.isFocused) {
            servicePasswordTextView.clearFocus()
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
                val x = event.rawX.toInt()
                val y = event.rawY.toInt()
                if (!outRect.contains(x, y)) {
                    v.clearFocus()
                    val et1: EditText? = findViewById(R.id.serviceNameTextView)
                    val et2: EditText? = findViewById(R.id.servicePasswordTextView)
                    val rect1 = Rect()
                    val rect2 = Rect()
                    val location1 = IntArray(2)
                    val location2 = IntArray(2)
                    et1!!.getLocationOnScreen(location1)
                    rect1.left = location1[0]
                    rect1.top = location1[1]
                    rect1.right = location1[0] + et1.width
                    rect1.bottom = location1[1] + et1.height
                    et2!!.getLocationOnScreen(location2)
                    rect2.left = location2[0]
                    rect2.top = location2[1]
                    rect2.right = location2[0] + et2.width
                    rect2.bottom = location2[1] + et2.height
                    if ((!rect1.contains(x, y)) && (!rect2.contains(x, y))) {
                        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                    }
                }
            }
        }
        return super.dispatchTouchEvent(event)

    }
}