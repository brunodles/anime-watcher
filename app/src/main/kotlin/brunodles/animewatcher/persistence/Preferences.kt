package brunodles.animewatcher.persistence

import android.content.Context
import android.preference.PreferenceManager

class Preferences(val context: Context) {

    companion object {
        private val KEY_URL = "URL"
    }

    fun setUrl(url:String) =
            preferences()
                    .edit()
                    .putString(KEY_URL, url)
                    .apply()

    fun getUrl() = preferences().getString(KEY_URL, null)

    private fun preferences() = PreferenceManager.getDefaultSharedPreferences(context)

}