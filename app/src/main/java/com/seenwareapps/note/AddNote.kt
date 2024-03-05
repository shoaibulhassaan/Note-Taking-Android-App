package com.seenwareapps.note


import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.seenwareapps.note.databinding.ActivityAddNoteBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddNote : AppCompatActivity() {
    private lateinit var  binding: ActivityAddNoteBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.doneBtn.setOnClickListener{

            if(binding.noteDescription.length() != 0){
                // Check if the activity is in "edit" mode
                if (intent.hasExtra("noteId")) {
                    val noteId = intent.getIntExtra("noteId", /* default value if not found */ 0)
                    val description = binding.noteDescription.text

                    // Split the text into words using whitespace as a delimiter
                    val words = description?.split("\\s+".toRegex())

                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                    val current = LocalDateTime.now().format(formatter)

                    // Take the first 5 words or all words if there are fewer than 5
                    val title = words?.take(5)?.joinToString(" ")

                    NoteDBHelper(this).updateNote(noteId,"$title", "$description", current)
                    Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show()
                    intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                else{
                    val description = binding.noteDescription.text

                    // Split the text into words using whitespace as a delimiter
                    val words = description?.split("\\s+".toRegex())

                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                    val current = LocalDateTime.now().format(formatter)

                    // Take the first 5 words or all words if there are fewer than 5
                    val title = words?.take(5)?.joinToString(" ")

                    NoteDBHelper(this).addNote("$title","$description", current)

                    intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }else{
                Toast.makeText(this, "Please add note description.", Toast.LENGTH_SHORT).show()
            }
        }

        // Check if the activity is in "edit" mode
        if (intent.hasExtra("noteId")) {
            val noteId = intent.getIntExtra("noteId", /* default value if not found */ 0)
            // Populate the fields with existing note data
            val existingNote = NoteDBHelper(this).getNoteById(noteId)
            existingNote?.let {
                binding.noteDescription.setText(existingNote.noteDescription)
            }
        }
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