package id.itborneo.facecare.core.networks

import com.google.firebase.database.FirebaseDatabase

object FaceCaseFirebase {
    private val database = FirebaseDatabase.getInstance()

    private val userRef = database.getReference("users")
    private val userIdentifyRef = database.getReference("usersIdentified")

    fun getUser(key: String) = userRef.child(key)
//    fun getUser() = userRef

    fun getIdentifyUser(key: String) = userIdentifyRef.child(key)
//    fun setIdentifiUser =

}