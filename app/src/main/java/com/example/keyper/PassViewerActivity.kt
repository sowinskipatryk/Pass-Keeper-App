package com.example.keyper

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class PassViewerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewer)

        val actionBar = supportActionBar
        actionBar!!.title = "Pass Viewer"
        supportActionBar?.setBackgroundDrawable(getDrawable(R.color.actionbar_color))

        val dbHandler = DBHandler(this)

        val passwordsList: ArrayList<Service> = dbHandler.returnPasswords()
        val rv: RecyclerView = findViewById(R.id.rvPasswords)
        val emptyPassViewerTextView = findViewById<TextView>(R.id.emptyPassViewerTextView)
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val pwAdapter = PasswordAdapter(this, passwordsList, rv, emptyPassViewerTextView)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.views, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.deleteButton) {
            Toast.makeText(baseContext, "delete", Toast.LENGTH_SHORT).show()
        } else {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
        return true;
    }
}