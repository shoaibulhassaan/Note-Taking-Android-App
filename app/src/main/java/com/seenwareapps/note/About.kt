package com.seenwareapps.note

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

class About : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.moreApp -> {
                // Handle the share action
                // For example, open a sharing dialog
                showShareDialog()
                true
            }
            R.id.aboutUs -> {
                intent = Intent(this, About::class.java)
                startActivity(intent)
                true
            }
            // Handle other menu items as needed
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showShareDialog() {
        Toast.makeText(this, "More App to play store", Toast.LENGTH_SHORT).show()
    }
}