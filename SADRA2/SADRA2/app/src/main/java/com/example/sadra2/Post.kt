package com.example.sadra2

import com.google.firebase.firestore.Exclude
import java.util.*

class Post(val post: String? = null, val date: Date? = null, val userName: String? = null, val likes: ArrayList<String>? = arrayListOf()) {
    @Exclude
    @set:Exclude
    @get:Exclude
    var uid: String? = null

    constructor() : this(null, null, null, null)
}