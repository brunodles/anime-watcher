package brunodles.animewatcher.persistence

import brunodles.animewatcher.BuildConfig
import brunodles.animewatcher.explorer.Episode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

object Firebase {

    private val REF_VIDEO = "videos"
    private val REF_USERS = "users"
    private val REF_HISTORY = "history"
    private val REF_NUMBER = "number"

    /** Return the reference to all videos mapped by the firebase **/
    fun videosRef() = firebaseRef().child(REF_VIDEO)

    /** Return the reference to a single video **/
    fun videoRef(url: String) = videosRef()
            .child(fixUrlToFirebase(url))
            .orderByChild(REF_NUMBER)

    fun addVideo(episode: Episode) =
            videosRef().child(fixUrlToFirebase(episode.link!!)).updateChildren(episode.toMap())

    fun addToHistory(url: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return
        history(currentUser).push().setValue(url)
    }

    fun history(currentUser: FirebaseUser) =
            firebaseRef().child(REF_USERS).child(currentUser.uid)
                    .child(REF_HISTORY)

    private fun firebaseRef() = FirebaseDatabase.getInstance().getReference(BuildConfig.BUILD_TYPE)
}
