package com.seenwareapps.note

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.seenwareapps.note.adapters.CustomAdapter
import com.seenwareapps.note.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var  binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.addBtn.setOnClickListener {
            intent = Intent(this, AddNote::class.java)
            startActivity(intent)
        }

        // share this



        // this creates a vertical layout Manager
        binding.recyclerView.layoutManager = LinearLayoutManager(this)


        // db helper
        val db = NoteDBHelper(this)

        // Get all notes from the database
        val dataFromDB = db.getAllNotes()

        // Create an ArrayList with the retrieved data
        val data = ArrayList(dataFromDB)

        if(data.isEmpty()){
            binding.noDataHintText.text = "Note List Empty!"
        }else{
            binding.noDataHintText.text = ""
        }

        // Create the adapter
        val adapter = CustomAdapter(data)

        // Get a reference to your RecyclerView
                val recyclerView = binding.recyclerView

        // Set the layout manager for the RecyclerView (assuming LinearLayoutManager)
                recyclerView.layoutManager = LinearLayoutManager(this)

        // Set the adapter for the RecyclerView
                recyclerView.adapter = adapter

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
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