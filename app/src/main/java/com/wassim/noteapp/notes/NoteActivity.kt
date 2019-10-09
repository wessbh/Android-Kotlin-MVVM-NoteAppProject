package com.wassim.noteapp.notes

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.wassim.noteapp.R

import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : AppCompatActivity() {
    private lateinit var nav: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        nav = Navigation.findNavController(
            this,
            R.id.navigation_graph
        )
    }

}
