package brunodles.animewatcher.persistence

import android.util.Log
import brunodles.animewatcher.explorer.Episode
import brunodles.rxfirebase.observableChildAdded
import brunodles.rxfirebase.singleObservable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import java.util.concurrent.TimeUnit

object Firebase {

    private const val TAG = "Firebase"
    private const val FETCH_LAST_FROM_HISTORY = false
    private const val REF_VIDEO = "videos"
    private const val REF_USERS = "users"
    private const val REF_HISTORY = "history"
    private const val REF_NUMBER = "number"
    private const val REF_SEARCH = "search"
    private const val REF_LAST_ON_HISTORY = "historyLast"
    private const val EMPTY_URL = ""

    /** Return the reference to all videos mapped by the firebase **/
    fun videosRef() = firebaseRef().child(REF_VIDEO)

    /** Return the reference to a single video **/
    fun videoRef(url: String) = videosRef()
        .child(fixUrlToFirebase(url))
        .orderByChild(REF_NUMBER)

    fun addVideo(episode: Episode) =
        videosRef().child(fixUrlToFirebase(episode.link)).updateChildren(episode.toMap())

    fun addToHistory(url: String) {
        val currentUser = currentUser()!!
        lastOnHistory(currentUser).subscribeBy(
            onSuccess = {
                Log.d(TAG, "addToHistory: last: $it equal to? $url")
                if (url != it)
                    history(currentUser).push().setValue(url)
            },
            onError = {
                Log.d(TAG, "addToHistory: failed to add to history", it)
            }
        )
    }

    private fun currentUser() = FirebaseAuth.getInstance().currentUser

    fun lastOnHistory(currentUser: FirebaseUser): Single<String> {
        val firstFromHistory = Firebase.history(currentUser)
            .limitToLast(1)
            .orderByKey()
            .observableChildAdded(String::class.java)
            .doOnNext { Log.d(TAG, "lastOnHistory: from: History") }
            .timeout(5, TimeUnit.SECONDS, Observable.just(EMPTY_URL))
            .first(EMPTY_URL)
        if (FETCH_LAST_FROM_HISTORY)
            return firstFromHistory
        return userRef(currentUser).child(REF_LAST_ON_HISTORY)
            .singleObservable(Episode::class.java)
            .doOnSuccess { Log.d(TAG, "lastOnHistory: from: lastField = $it") }
            .map { it.link }
            .timeout(5, TimeUnit.SECONDS, firstFromHistory)
            .onErrorResumeNext(firstFromHistory)
    }

    fun history(currentUser: FirebaseUser) =
        userRef(currentUser)
            .child(REF_HISTORY)

    fun addToSearchHistory(query: String) {
        val currentUser = currentUser()!!
        searchRef(currentUser)
            .push()
            .setValue(query)
    }

    private fun searchRef(currentUser: FirebaseUser) =
        userRef(currentUser).child(REF_SEARCH)

    fun searchHistory() = searchRef(currentUser()!!)

    private fun userRef(currentUser: FirebaseUser) =
        firebaseRef().child(REF_USERS).child(currentUser.uid)

    private fun firebaseRef() = FirebaseDatabase.getInstance().reference
}
