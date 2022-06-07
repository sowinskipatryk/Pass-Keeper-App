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

    }

    private fun showPasswords() {
        val passwordsList : ArrayList<Service> = dbHandler.getPasswords()
        val adapter = PasswordAdapter(passwordsList)
        val rv : RecyclerView = findViewById(R.id.passwordList)
        rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rv.adapter = adapter
    }

    override fun onResume() {
        showPasswords()
        super.onResume()
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