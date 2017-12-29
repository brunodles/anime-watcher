package brunodles.animewatcher.persistence

import android.content.Context
import android.preference.PreferenceManager

class Preferences(private val context: Context) {

    companion object {
        private val KEY_URL = "URL"
        private val KEY_IMAGE_URL = "IMAGE_URL"
    }

    fun setUrl(url: String) =
            preferences()
                    .edit()
                    .putString(KEY_URL, url)
                    .apply()

    fun getUrl() = preferences().getString(KEY_URL, null)

    fun setLastAnimeImage(url: String) =
            preferences()
                    .edit()
                    .putString(KEY_IMAGE_URL, url)
                    .apply()

    fun getLastAnimeImage() =
            preferences()
                    .getString(KEY_IMAGE_URL, "")

    private fun preferences() = PreferenceManager.getDefaultSharedPreferences(context)

}