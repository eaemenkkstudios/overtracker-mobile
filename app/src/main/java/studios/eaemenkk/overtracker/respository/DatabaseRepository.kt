package studios.eaemenkk.overtracker.respository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import studios.eaemenkk.overtracker.domain.DatabaseObject

class DatabaseRepository {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun followedPlayers() {
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post = dataSnapshot.value
                println(post)
                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                // ...
            }
        }
        mDatabase.child("accounts").child(mAuth.currentUser?.uid.toString()).addValueEventListener(listener)
    }
}
