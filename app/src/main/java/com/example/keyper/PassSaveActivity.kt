package com.example.keyper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.keyper.DBHelper

class PassSaveActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pass_save)

        val actionBar = supportActionBar
        actionBar!!.title = "Add password"
        actionBar.setDisplayHomeAsUpEnabled(true)

        val serviceNameTextView = findViewById<TextView>(R.id.serviceNameTextView)
        val servicePasswordTextView = findViewById<TextView>(R.id.servicePasswordTextView)
        val savePasswordButton = findViewById<Button>(R.id.savePasswordButton)
        val savingInfoTextView = findViewById<TextView>(R.id.savingInfoTextView)
        val db = DBHelper(this)

        savePasswordButton.setOnClickListener {
            var serviceNameText = serviceNameTextView.getText().toString()
            var servicePasswordText = servicePasswordTextView.getText().toString()
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