package brunodles.animewatcher.persistence

import com.google.firebase.database.FirebaseDatabase

object Firebase {

    private val REF_VIDEO = "video"

    fun video(url: String) = FirebaseDatabase.getInstance().getReference(REF_VIDEO)
            .child(fixUrlToFirebase(url))
}
