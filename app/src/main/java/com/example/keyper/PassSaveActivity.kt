package com.example.keyper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class PassSaveActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pass_save)

        val actionBar = supportActionBar
        actionBar!!.title = "Pass Creator"
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

//        serviceNameTextView.setOnKeyListener(View.OnKeyListener{ v, keyCode, event ->
//            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
//                serviceNameTextView.clearFocus()
//                servicePasswordTextView.requestFocus()
//                return@OnKeyListener true
//            }
//            false
//            })

        savePasswordButton.setOnClickListener {
            var serviceNameText = serviceNameTextView.text.toString()
            serviceNameText = serviceNameText.lowercase()
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

}