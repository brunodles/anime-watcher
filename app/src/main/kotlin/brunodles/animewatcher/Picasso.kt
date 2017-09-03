package brunodles.animewatcher

import android.widget.ImageView
import com.squareup.picasso.Picasso

fun loadImageInto(url: String?, image: ImageView) {
    if (url.isNullOrEmpty()) return
    Picasso.Builder(image.context).indicatorsEnabled(true)
            .loggingEnabled(true)
            .build()
            .load(url)
            .placeholder(R.drawable.loading)
            .error(R.drawable.error)
            .into(image)
}