package com.example.keyper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.keyper.DBHelper

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
        val db = DBHelper(this)
        val bundle : Bundle? = intent.extras
        if (bundle != null) {
            servicePasswordTextView.text = bundle.getString("passw")
        }
//        val generatedPasswordString = intent.getStringExtra("passwordString").toString()
//        servicePasswordTextView.text = generatedPasswordString

//        serviceNameTextView.setOnKeyListener(View.OnKeyListener{v, keyCode, event ->
//            if ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) {
//            serviceNameTextView.clearFocus()
//            }
//        })


        savePasswordButton.setOnClickListener {
            var serviceNameText = serviceNameTextView.getText().toString()
            serviceNameText = serviceNameText.lowercase()
            var servicePasswordText = servicePasswordTextView.getText().toString()
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

}