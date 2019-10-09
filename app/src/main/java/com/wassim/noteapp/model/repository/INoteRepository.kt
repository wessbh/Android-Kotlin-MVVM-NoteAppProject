package com.wassim.noteapp.model.repository

import com.wassim.noteapp.model.Note
import com.wassim.noteapp.common.Result

interface INoteRepository {
    suspend fun getNoteById(noteId: String): Result<Exception, Note>
    suspend fun getNotes(): Result<Exception, List<Note>>
    suspend fun deleteNote(note: Note): Result<Exception, Unit>
    suspend fun updateNote(note: Note): Result<Exception, Unit>
}