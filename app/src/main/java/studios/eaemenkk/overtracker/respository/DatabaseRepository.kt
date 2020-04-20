package studios.eaemenkk.overtracker.respository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DatabaseRepository {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun followPlayer(playerId: String, callback: (result: Task<Void>) -> Unit) {
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.value == null) {
                    val key = mDatabase.child("accounts").child(mAuth.currentUser?.uid.toString()).push().key
                    val childUpdates = HashMap<String, Any>()
                    if(key == null) {
                        return
                    }
                    childUpdates["/accounts/${mAuth.currentUser?.uid.toString()}/following/$key"] = playerId
                    mDatabase.updateChildren(childUpdates).addOnCompleteListener { task -> callback(task) }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        val query = mDatabase.child("accounts").child(mAuth.currentUser?.uid.toString()).child("following").orderByValue().equalTo(playerId)
        query.addListenerForSingleValueEvent(listener)
    }

    fun unfollowPlayer(playerId: String, callback: (result: Task<Void>) -> Unit) {
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.value != null) {
                    val childUpdates = HashMap<String, Any?>()
                    val player = dataSnapshot.value as HashMap<String, String>
                    player.forEach{ p -> childUpdates["/accounts/${mAuth.currentUser?.uid.toString()}/following/${p.key}"] = null}
                   mDatabase.updateChildren(childUpdates).addOnCompleteListener { task -> callback(task) }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        val query = mDatabase.child("accounts").child(mAuth.currentUser?.uid.toString()).child("following").orderByValue().equalTo(playerId)
        query.addListenerForSingleValueEvent(listener)
    }

    fun isFollowing(playerId: String, callback: (result: Boolean) -> Unit) {
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                callback(dataSnapshot.value != null)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        val query = mDatabase.child("accounts").child(mAuth.currentUser?.uid.toString()).child("following").orderByValue().equalTo(playerId)
        query.addListenerForSingleValueEvent(listener)
    }
}
