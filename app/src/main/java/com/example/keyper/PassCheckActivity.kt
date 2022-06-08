package com.example.keyper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PassCheckActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pass_check)

        val actionBar = supportActionBar
        actionBar!!.title = "Pass Viewer"

        val dbHandler = DBHandler(this)

        val passwordsList : ArrayList<Service> = dbHandler.returnPasswords()
        val rv : RecyclerView = findViewById(R.id.rvPasswords)
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val pwAdapter = PasswordAdapter(this, passwordsList, rv)

        rv.hasFixedSize()
        rv.layoutManager = layoutManager
        rv.adapter = pwAdapter

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