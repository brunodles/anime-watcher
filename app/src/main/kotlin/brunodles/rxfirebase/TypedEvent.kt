package brunodles.rxfirebase

import com.google.firebase.database.*
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import java.lang.IllegalArgumentException

enum class EventType {
    MOVED, CHANGED, ADDED, REMOVED
}

class TypedEvent<ELEMENT>(val event: EventType, val element: ELEMENT, val key: String)

fun <T> DatabaseReference.typedChildObserver(valueClass: Class<T>): Observable<TypedEvent<T>> {
    return Observable.create { emitter ->
        this.addChildEventListener(TypedEventParseListener(emitter, valueClass))
    }
}

fun <T> Query.typedChildObserver(valueClass: Class<T>): Observable<TypedEvent<T>> {
    return Observable.create { emitter ->
        this.addChildEventListener(TypedEventParseListener(emitter, valueClass))
    }
}

private class TypedEventParseListener<T>(val emitter: ObservableEmitter<TypedEvent<T>>, val valueClass: Class<T>) :
        ChildEventListener {
    override fun onCancelled(p0: DatabaseError?) {
        if (p0 == null)
            emitter.onComplete()
        else
            emitter.onError(p0.toException())
    }

    override fun onChildMoved(p0: DataSnapshot?, p1: String?) = sendEvent(p0, EventType.MOVED)

    override fun onChildChanged(p0: DataSnapshot?, p1: String?) = sendEvent(p0, EventType.CHANGED)

    override fun onChildAdded(p0: DataSnapshot?, p1: String?) = sendEvent(p0, EventType.ADDED)

    override fun onChildRemoved(p0: DataSnapshot?) = sendEvent(p0, EventType.REMOVED)

    private fun sendEvent(p0: DataSnapshot?, type: EventType) {
        if (p0 == null || !p0.exists()) {
            emitter.onError(IllegalArgumentException("Value not found on requested reference."))
            return
        }
        try {
            val value = p0.getValue(valueClass)
            if (value != null) {
                emitter.onNext(TypedEvent(type, value, p0.key))
            }
        } catch (e: Exception) {
            emitter.onError(e)
        }
    }

}
