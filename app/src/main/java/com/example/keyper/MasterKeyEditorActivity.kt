package com.example.keyper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView

class MasterKeyEditorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_masterkey_editor)

        val actionBar = supportActionBar
        actionBar!!.title = "Master Key Editor"
        actionBar.setDisplayHomeAsUpEnabled(true)

        val updateMasterKeyEditText = findViewById<TextView>(R.id.updateMasterKeyEditText)
        val updateMasterKeyButton = findViewById<Button>(R.id.updateMasterKeyButton)
        val updateMasterKeyErrorTextView = findViewById<TextView>(R.id.updateMasterKeyErrorTextView)
        val db = DBHandler(this)

        updateMasterKeyButton.setOnClickListener {
            val servicePasswordText = updateMasterKeyEditText.text.toString()
            if (servicePasswordText.isEmpty()) {
                updateMasterKeyErrorTextView.text = " Fill in the new Master Key!"
                updateMasterKeyErrorTextView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_warning_24, 0, 0, 0
                )
            } else {
                val successfulUpdate = db.updateMasterKey(servicePasswordText)
                if (successfulUpdate == true) {
                    updateMasterKeyErrorTextView.text = " Master Key updated!"
                    updateMasterKeyErrorTextView.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_baseline_bookmark_added_24, 0, 0, 0
                    )
                } else {
                    updateMasterKeyErrorTextView.text = " Error occurred!"
                    updateMasterKeyErrorTextView.setCompoundDrawablesWithIntrinsicBounds(
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