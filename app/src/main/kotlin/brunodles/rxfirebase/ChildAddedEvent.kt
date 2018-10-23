package brunodles.rxfirebase

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import io.reactivex.Observable
import io.reactivex.ObservableEmitter

fun DatabaseReference.observableChildAdded(): Observable<DataSnapshot> {
    return Observable.create { emitter ->
        this.addChildEventListener(ChildAddedListener(emitter))
    }
}

fun <T> DatabaseReference.observableChildAdded(valueClass: Class<T>): Observable<T> {
    return Observable.create { emitter ->
        this.addChildEventListener(ChildAddedParseListener(emitter, valueClass))
    }
}

fun <T> Query.observableChildAdded(valueClass: Class<T>): Observable<T> {
    return Observable.create { emitter ->
        this.addChildEventListener(ChildAddedParseListener(emitter, valueClass))
    }
}

private class ChildAddedListener(val emitter: ObservableEmitter<DataSnapshot>) :
    ChildEventListener {
    override fun onChildMoved(p0: DataSnapshot, p1: String?) {
    }

    override fun onChildChanged(p0: DataSnapshot, p1: String?) {
    }

    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
        emitter.onNext(p0)
    }

    override fun onChildRemoved(p0: DataSnapshot) {
    }

    override fun onCancelled(p0: DatabaseError) {
        emitter.onError(p0.toException())
    }
}

private class ChildAddedParseListener<T>(
    val emitter: ObservableEmitter<T?>,
    val valueClass: Class<T>
) : ChildEventListener {
    override fun onChildMoved(p0: DataSnapshot, p1: String?) {
    }

    override fun onChildChanged(p0: DataSnapshot, p1: String?) {
    }

    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
        if (!p0.exists()) {
            emitter.onError(IllegalArgumentException("Value not found on requested reference."))
            return
        }
        try {
            val value = p0.getValue(valueClass)
            if (value != null)
                emitter.onNext(value)
        } catch (e: Exception) {
            emitter.onError(e)
        }
    }

    override fun onChildRemoved(p0: DataSnapshot) {
    }

    override fun onCancelled(p0: DatabaseError) {
        emitter.onError(p0.toException())
    }
}