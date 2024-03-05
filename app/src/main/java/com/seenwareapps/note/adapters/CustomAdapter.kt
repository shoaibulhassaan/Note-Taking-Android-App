package com.seenwareapps.note.adapters

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.seenwareapps.note.AddNote
import com.seenwareapps.note.NoteDBHelper
import com.seenwareapps.note.R
import com.seenwareapps.note.models.Note

class CustomAdapter(private var mList: List<Note>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    lateinit var onResponse: (r : ResponseType) -> Unit

    enum class ResponseType {
        YES, NO
    }


    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_card_view, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = mList[position]

        // sets the text to the textview from our itemHolder class
        holder.title.text = itemsViewModel.noteTitle
        holder.description.text = itemsViewModel.noteDescription
        holder.date.text = itemsViewModel.noteDate

        val context = holder.deleteBtn.context

        holder.deleteBtn.setOnClickListener {

            fun confirmDelete( listener: (r : ResponseType) -> Unit){
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Do you confirm deletion?")
                builder.setMessage(itemsViewModel.noteTitle)
                builder.setIcon(android.R.drawable.ic_delete)

                onResponse =  listener

                // performing positive action
                builder.setPositiveButton("Yes") { _, _ ->
                    onResponse(ResponseType.YES)
                }

                // performing negative action
                builder.setNegativeButton("No") { _, _ ->
                    onResponse(ResponseType.NO)
                }

                // Create the AlertDialog
                val alertDialog: AlertDialog = builder.create()

                // Set other dialog properties
                alertDialog.setCancelable(false)
                alertDialog.show()

            }

            confirmDelete { responseType ->
                // Handle the response based on the enum ResponseType
                when (responseType) {
                    ResponseType.YES -> {
                        // User confirmed deletion, perform deletion logic here
                        // For example, you can call your existing logic for deletion
                        val noteIdToDelete = itemsViewModel.noteId
                        val db = NoteDBHelper(context)
                        val isDeleted = db.deleteNote(noteIdToDelete)

                        if (isDeleted) {
                            val newData = db.getAllNotes()
                            updateData(newData)
                        } else {
                            Toast.makeText(context, "Note Not deleted", Toast.LENGTH_SHORT).show()
                        }
                    }
                    ResponseType.NO -> {
                        // User declined deletion, handle accordingly
                        // You can add any specific logic here
                    }
                }
            }
        }

        // update note
        holder.updateBtn.setOnClickListener{
            val intent = Intent(context, AddNote::class.java)
            intent.putExtra("noteId", itemsViewModel.noteId)
            (context as Activity).startActivityForResult(intent, itemsViewModel.noteId)
        }

        //sharing note to social media
        holder.shareBtn.setOnClickListener{
            val noteIdToShare = itemsViewModel.noteId
            val note = NoteDBHelper(context).getNoteById(noteIdToShare)

            // Example text to share
            val shareText = "Hello, I am sharing this note created with Note app, download and share your note too" +
                    "\n ${note?.noteDescription}"

            // Create a sharing intent
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }

            // Create a chooser intent to show the user a list of available sharing options
            val shareIntent = Intent.createChooser(sendIntent, "My useful note")
            context.startActivity(Intent.createChooser(shareIntent, "Share Note"))
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val shareBtn : ImageView = itemView.findViewById(R.id.share_btn)
        val deleteBtn : LinearLayout = itemView.findViewById(R.id.delete_btn)
        val updateBtn : LinearLayout = itemView.findViewById(R.id.noteCardView)
        val title: TextView = itemView.findViewById(R.id.noteTitle)
        val description: TextView = itemView.findViewById(R.id.noteDescription)
        val date: TextView = itemView.findViewById(R.id.noteDate)
    }

    private fun updateData(newData: List<Note>) {
        val mutableList = mutableListOf<Note>()
        mutableList.addAll(newData)
        mList = mutableList
        notifyDataSetChanged()
    }
}
