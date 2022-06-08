package com.example.keyper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PassCheckActivity : AppCompatActivity() {

    companion object {
        lateinit var dbHandler : DBHandler
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pass_check)

        val actionBar = supportActionBar
        actionBar!!.title = "Pass Viewer"

        val passwordsList : ArrayList<Service> = dbHandler.getPasswords()
        val rv : RecyclerView = findViewById(R.id.passwordList)
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

}