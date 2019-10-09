package com.wassim.noteapp.model.implementations

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.wassim.noteapp.common.*
import com.wassim.noteapp.model.FirebaseNote
import com.wassim.noteapp.model.Note
import com.wassim.noteapp.model.NoteDao
import com.wassim.noteapp.model.repository.INoteRepository

private const val COLLECTION_NAME = "notes"

class NoteRepoImpl(
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    val remote: FirebaseFirestore = FirebaseFirestore.getInstance(),
    val local: NoteDao
) : INoteRepository {


    override suspend fun getNoteById(noteId: String): Result<Exception, Note> {
        return if (hasActiveUser()) getRemoteNote(noteId)
        else getLocalNote(noteId)
    }

    override suspend fun deleteNote(note: Note): Result<Exception, Unit> {
        return if (hasActiveUser()) deleteRemoteNote(note)
        else deleteLocalNote(note)
    }

    override suspend fun updateNote(note: Note): Result<Exception, Unit> {
        return if (hasActiveUser()) updateRemoteNote(note)
        else updateLocalNote(note)
    }

    override suspend fun getNotes(): Result<Exception, List<Note>> {
        return if (hasActiveUser()) getRemoteNotes()
        else getLocalNotes()
    }


    private fun hasActiveUser(): Boolean = (firebaseAuth.currentUser != null)


    private fun resultToNoteList(result: QuerySnapshot?): Result<Exception, List<Note>> {
        val noteList = mutableListOf<Note>()

        result?.forEach { documentSnapshop ->
            noteList.add(documentSnapshop.toObject(FirebaseNote::class.java).toNote)
        }

        return Result.build {
            noteList
        }
    }


    /* Remote Datasource */

    private suspend fun getRemoteNotes(): Result<Exception, List<Note>> {
        var reference = remote.collection(COLLECTION_NAME)

        return try {
            val task = awaitTaskResult(reference.get())

            return resultToNoteList(task)
        } catch (exception: Exception) {
            Result.build { throw exception }
        }
    }

    private suspend fun getRemoteNote(id: String): Result<Exception, Note> {
        var reference = remote.collection(COLLECTION_NAME)
            .document(id)

        return try {
            val task = awaitTaskResult(reference.get())

            Result.build {
                task.toObject(FirebaseNote::class.java)?.toNote ?: throw Exception()
            }
        } catch (exception: Exception) {
            Result.build { throw exception }
        }

    }

    private suspend fun deleteRemoteNote(note: Note): Result<Exception, Unit> = Result.build {
        awaitTaskCompletable(
            remote.collection(COLLECTION_NAME)
                .document(note.creationDate)
                .delete()
        )
    }

    private suspend fun updateRemoteNote(note: Note): Result<Exception, Unit> {
        return try {
            awaitTaskCompletable(
                remote.collection(COLLECTION_NAME)
                    .document(note.creationDate)
                    .set(note.toFirebaseNote)
            )

            Result.build { Unit }

        } catch (exception: Exception) {
            Result.build { throw exception }
        }
    }

    /* Local Datasource */
    private suspend fun getLocalNotes(): Result<Exception, List<Note>> = Result.build {
        local.getNotes().toNoteListFromRoomNote()
    }

    private suspend fun getLocalNote(id: String): Result<Exception, Note> = Result.build {
        local.getNoteById(id).toNote
    }

    private suspend fun deleteLocalNote(note: Note): Result<Exception, Unit> = Result.build {
        local.deleteNote(note.toRoomNote)
        Unit
    }

    private suspend fun updateLocalNote(note: Note): Result<Exception, Unit> = Result.build {
        local.insertOrUpdateNote(note.toRoomNote)
        Unit
    }



}