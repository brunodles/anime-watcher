package brunodles.animewatcher

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.squareup.picasso.Picasso
import java.lang.ref.WeakReference

object ImageLoader {
    private var picassoSingleton: WeakReference<Picasso>? = null

    private fun picasso(context: Context): Picasso {
        if (picassoSingleton?.get() == null)
            picassoSingleton = WeakReference(Picasso.Builder(context)
                    .indicatorsEnabled(true)
                    .loggingEnabled(true)
                    .defaultBitmapConfig(Bitmap.Config.RGB_565)
                    .build())
        return picassoSingleton?.get()!!
    }

    fun loadImageInto(url: String?, image: ImageView) {
        if (url.isNullOrEmpty()) return
        picasso(image.context)
                .load(url)
                .placeholder(R.drawable.img_loading)
                .error(R.drawable.img_error)
                .into(image)
    }
}