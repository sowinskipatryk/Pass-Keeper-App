package com.example.keyper

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
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

        updatePasswordButton.setOnClickListener {
            var serviceNameText = updateServiceNameTextView.text.toString()
            serviceNameText = serviceNameText.lowercase().capitalize()
            var servicePasswordText = updateServicePasswordTextView.text.toString()
            if ((serviceNameText.isEmpty()) || (servicePasswordText.isEmpty())) {
                updatingInfoTextView.text = " Fill in all input fields!"
                updatingInfoTextView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_warning_24, 0, 0, 0
                )
            } else {
                var successfulUpdate = db.updateServicePassword(serviceNameText, servicePasswordText)
                if (successfulUpdate == true) {
                    updatingInfoTextView.text = " Password updated!"
                    updatingInfoTextView.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_baseline_bookmark_added_24, 0, 0, 0
                    )
                } else {
                    updatingInfoTextView.text = " Updating error occurred!"
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

}