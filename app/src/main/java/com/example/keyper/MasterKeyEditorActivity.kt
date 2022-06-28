package com.example.keyper

import android.content.Context
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
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

class MasterKeyEditorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_masterkey_editor)

        val actionBar = supportActionBar
        supportActionBar?.setBackgroundDrawable(getDrawable(R.color.actionbar_color))
        actionBar!!.title = getString(R.string.main_menu)
        actionBar.setDisplayHomeAsUpEnabled(true)

        val updateMasterKeyEditText = findViewById<TextView>(R.id.updateMasterKeyEditText)
        val updateMasterKeyButton = findViewById<Button>(R.id.updateMasterKeyButton)
        val updateMasterKeyErrorTextView = findViewById<TextView>(R.id.updateMasterKeyErrorTextView)
        val db = DBHandler(this)

        updateMasterKeyEditText.setOnKeyListener(View.OnKeyListener{ v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && updateMasterKeyEditText.isFocused) {
                this.currentFocus?.let { view ->
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(view.windowToken, 0)
                }
                updateMasterKeyEditText.clearFocus()
                return@OnKeyListener true
            }
            false
        })

        updateMasterKeyEditText.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (!hasFocus) {
                    updateMasterKeyEditText.clearFocus()
                }
            }
        })

        updateMasterKeyButton.setOnClickListener {
            val servicePasswordText = updateMasterKeyEditText.text.toString()
            if (servicePasswordText.isEmpty()) {
                updateMasterKeyErrorTextView.text = getString(R.string.fill_new_master_key)
                updateMasterKeyErrorTextView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_warning_24, 0, 0, 0
                )
            } else {
                val successfulUpdate = db.updateMasterKey(servicePasswordText)
                if (successfulUpdate == true) {
                    updateMasterKeyErrorTextView.text = getString(R.string.masterkey_update_successful)
                    updateMasterKeyErrorTextView.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_baseline_bookmark_added_24, 0, 0, 0
                    )
                } else {
                    updateMasterKeyErrorTextView.text = getString(R.string.update_error)
                    updateMasterKeyErrorTextView.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_baseline_warning_24, 0, 0, 0
                    )
                }
                updateMasterKeyEditText.text = getString(R.string.empty_string)
            }
            Handler().postDelayed({
                updateMasterKeyErrorTextView.text = getString(R.string.empty_string)
                updateMasterKeyErrorTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.empty, 0, 0, 0)
            }, 2000)
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
        val updateMasterKeyEditText = findViewById<EditText>(R.id.updateMasterKeyEditText)
        if (updateMasterKeyEditText.isFocused) {
            updateMasterKeyEditText.clearFocus()
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