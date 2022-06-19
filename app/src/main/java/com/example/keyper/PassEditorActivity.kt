package com.example.keyper

import android.content.Context
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class PassEditorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        val actionBar = supportActionBar
        actionBar!!.title = "Pass Editor"
        supportActionBar?.setBackgroundDrawable(getDrawable(R.color.actionbar_color))
        actionBar.setDisplayHomeAsUpEnabled(true)

        val updateServiceNameTextView = findViewById<TextView>(R.id.updateServiceNameTextView)
        val updateServicePasswordTextView = findViewById<TextView>(R.id.updateServicePasswordTextView)
        val updatePasswordButton = findViewById<Button>(R.id.updatePasswordButton)
        val updatingInfoTextView = findViewById<TextView>(R.id.updatingInfoTextView)
        val db = DBHandler(this)

        updateServiceNameTextView.setOnKeyListener(View.OnKeyListener{ v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && updateServiceNameTextView.isFocused && event.action == KeyEvent.ACTION_UP) {
                updateServiceNameTextView.clearFocus()
                updateServicePasswordTextView.requestFocus()
                return@OnKeyListener true
            }
            false
        })

        updateServiceNameTextView.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (!hasFocus) {
                    updateServiceNameTextView.clearFocus()
                }
            }
        })

        updateServicePasswordTextView.setOnKeyListener(View.OnKeyListener{ v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && updateServicePasswordTextView.isFocused && event.action == KeyEvent.ACTION_UP) {
                this.currentFocus?.let { view ->
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(view.windowToken, 0)
                }
                updateServicePasswordTextView.clearFocus()
                return@OnKeyListener true
            }
            false
        })

        updateServicePasswordTextView.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (!hasFocus) {
                    updateServicePasswordTextView.clearFocus()
                }
            }
        })

        updatePasswordButton.setOnClickListener {
            var serviceNameText = updateServiceNameTextView.text.toString()
            serviceNameText = serviceNameText.lowercase().capitalize()
            val servicePasswordText = updateServicePasswordTextView.text.toString()
            if ((serviceNameText.isEmpty()) || (servicePasswordText.isEmpty())) {
                updatingInfoTextView.text = getString(R.string.missing_fields_error)
                updatingInfoTextView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_warning_24, 0, 0, 0
                )
            } else {
                val successfulUpdate = db.updateServicePassword(serviceNameText, servicePasswordText)
                if (successfulUpdate == true) {
                    updatingInfoTextView.text = getString(R.string.update_successful)
                    updatingInfoTextView.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_baseline_bookmark_added_24, 0, 0, 0
                    )
                } else {
                    updatingInfoTextView.text = getString(R.string.update_error)
                    updatingInfoTextView.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_baseline_warning_24, 0, 0, 0
                    )
                }
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

    override fun onBackPressed() {
        val updateServiceNameTextView = findViewById<EditText>(R.id.updateServiceNameTextView)
        val updateServicePasswordTextView = findViewById<EditText>(R.id.updateServicePasswordTextView)
        if (updateServiceNameTextView.isFocused) {
            updateServiceNameTextView.clearFocus()
        } else if (updateServicePasswordTextView.isFocused) {
            updateServicePasswordTextView.clearFocus()
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
                    val et1: EditText? = findViewById(R.id.updateServiceNameTextView)
                    val et2: EditText? = findViewById(R.id.updateServicePasswordTextView)
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