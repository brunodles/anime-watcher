package brunodles.rxfirebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import java.lang.IllegalArgumentException

fun <T> DatabaseReference.singleObservable(valueClass: Class<T>): Observable<T?> {
    return Observable.create { emitter ->
        this.addListenerForSingleValueEvent(SingleEventParseListener(emitter, valueClass))
    }
}

fun DatabaseReference.singleObservable(): Observable<DataSnapshot> {
    return Observable.create { emitter ->
        this.addListenerForSingleValueEvent(SingleEventListener(emitter))
    }
}

private class SingleEventListener(val emitter: ObservableEmitter<DataSnapshot>) : ValueEventListener {

    override fun onCancelled(p0: DatabaseError?) {
        emitter.onError(p0?.toException())
    }

    override fun onDataChange(p0: DataSnapshot?) {
        emitter.onNext(p0)
        emitter.onComplete()
    }

}

private class SingleEventParseListener<T>(val emitter: ObservableEmitter<T?>, val valueClass: Class<T>) : ValueEventListener {

    override fun onCancelled(p0: DatabaseError?) {
        emitter.onError(p0?.toException())
    }

    override fun onDataChange(p0: DataSnapshot?) {
        if (p0 == null || !p0.exists()) {
            emitter.onError(IllegalArgumentException("Value not found on requested reference."))
            return
        }
        emitter.onNext(p0.getValue(valueClass))
        emitter.onComplete()
    }
}