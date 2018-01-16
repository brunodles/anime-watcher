package brunodles.rxpicasso

import android.graphics.Bitmap
import com.squareup.picasso.RequestCreator
import io.reactivex.Single

fun RequestCreator.asSingle(): Single<Bitmap> {
    return Single.create { subject ->
        try {
            subject.onSuccess(this.get())
        } catch (error: Throwable) {
            subject.onError(error)
        }
    }
}