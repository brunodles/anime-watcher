package brunodles.animewatcher.persistence

import android.util.Log
import brunodles.animewatcher.BuildConfig
import brunodles.animewatcher.explorer.Episode
import brunodles.rxfirebase.observableChildAdded
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import java.util.concurrent.TimeUnit

object Firebase {

    private val REF_VIDEO = "videos"
    private val REF_USERS = "users"
    private val REF_HISTORY = "history"
    private val REF_NUMBER = "number"
    private val EMPTY_URL = ""

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

        val history = Firebase.history(currentUser)
        history.limitToLast(1)
                .orderByKey()
                .observableChildAdded(String::class.java)
                .timeout(5, TimeUnit.SECONDS, Observable.just(EMPTY_URL))
                .first(EMPTY_URL)
                .subscribeBy(
                        onSuccess = {
                            if (url != it)
                                history.push().setValue(url)
                        },
                        onError = {
                            Log.d("Firebase", "addToHistory: failed to add to history", it)
                        }
                )
    }

    fun history(currentUser: FirebaseUser) =
            firebaseRef().child(REF_USERS).child(currentUser.uid)
                    .child(REF_HISTORY)

    private fun firebaseRef() = FirebaseDatabase.getInstance().getReference(BuildConfig.BUILD_TYPE)
}
