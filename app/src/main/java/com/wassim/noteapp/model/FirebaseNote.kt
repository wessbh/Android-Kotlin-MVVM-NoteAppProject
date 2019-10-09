package com.wassim.noteapp.model

import androidx.annotation.Keep

@Keep
data class FirebaseNote(
        var creationDate: String? = "",
        var contents: String? = "",
        var upVotes: Int? = 0,
        var imageurl: String? = "",
        var creator: String? = ""
)