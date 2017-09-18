package brunodles.animewatcher.persistence

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

object Firebase {

    private val REF_VIDEO = "video"
    private val REF_USERS = "users"
    private val REF_HISTORY = "history"

    fun video(url: String) = FirebaseDatabase.getInstance().getReference(REF_VIDEO)
            .child(fixUrlToFirebase(url))

    fun addToHistory(url: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return

        history(currentUser).push().setValue(url)
    }

    fun history(currentUser: FirebaseUser) =
            FirebaseDatabase.getInstance().getReference(REF_USERS).child(currentUser.uid)
                    .child(REF_HISTORY)
}
