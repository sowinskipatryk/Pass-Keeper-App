package com.example.keyper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.keyper.DBHelper

class PassUpdateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pass_update)

        val actionBar = supportActionBar
        actionBar!!.title = "Pass Editor"
        actionBar.setDisplayHomeAsUpEnabled(true)

        val updateServiceNameTextView = findViewById<TextView>(R.id.updateServiceNameTextView)
        val updateServicePasswordTextView = findViewById<TextView>(R.id.updateServicePasswordTextView)
        val updatePasswordButton = findViewById<Button>(R.id.updatePasswordButton)
        val updatingInfoTextView = findViewById<TextView>(R.id.updatingInfoTextView)
        val db = DBHelper(this)

        updatePasswordButton.setOnClickListener {
            var serviceNameText = updateServiceNameTextView.getText().toString()
            serviceNameText = serviceNameText.lowercase()
            var servicePasswordText = updateServicePasswordTextView.getText().toString()
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

}