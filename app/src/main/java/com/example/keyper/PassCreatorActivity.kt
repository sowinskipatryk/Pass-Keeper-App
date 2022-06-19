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
        val bundle : Bundle? = intent.extras
        if (bundle != null) {
            servicePasswordTextView.text = bundle.getString("passw")
        }

        serviceNameTextView.setOnKeyListener(View.OnKeyListener{ v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && serviceNameTextView.isFocused && event.action == KeyEvent.ACTION_UP) {
                serviceNameTextView.clearFocus()
                servicePasswordTextView.requestFocus()
                return@OnKeyListener true
            }
            false
            })

        servicePasswordTextView.setOnKeyListener(View.OnKeyListener{ v, keyCode, event ->
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

        savePasswordButton.setOnClickListener {
            var serviceNameText = serviceNameTextView.text.toString()
            serviceNameText = serviceNameText.lowercase().capitalize()
            var servicePasswordText = servicePasswordTextView.text.toString()
            if ((serviceNameText.isEmpty()) || (servicePasswordText.isEmpty())) {
                savingInfoTextView.text = " Fill in all input fields!"
                savingInfoTextView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_warning_24, 0, 0, 0
                )
            } else {
                var successfulInsertion = db.insertServicePassword(serviceNameText, servicePasswordText)
                if (successfulInsertion == true) {
                    savingInfoTextView.text = " Password saved!"
                    savingInfoTextView.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_baseline_bookmark_added_24, 0, 0, 0
                    )
                } else {
                    savingInfoTextView.text = " Saving error occurred!"
                    savingInfoTextView.setCompoundDrawablesWithIntrinsicBounds(
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

}