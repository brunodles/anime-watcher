package brunodles.animewatcher

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.preference.PreferenceManager
import android.util.Log
import bruno.animewatcher.explorer.AnimeExplorer
import brunodles.animacurse.AnimaCurseFactory
import brunodles.animesproject.AnimesProjectFactory
import brunodles.anitubex.AnitubexFactory
import java.util.*

class CheckUrl(private val intent: Intent, private val context: Context, private val function: (AnimeExplorer) -> Unit) :
        AsyncTask<Void, Void, AnimeExplorer?>() {

    val factories by lazy { Arrays.asList(AnitubexFactory, AnimaCurseFactory, AnimesProjectFactory) }
    val preference by lazy { PreferenceManager.getDefaultSharedPreferences(context) }

    override fun doInBackground(vararg params: Void?): AnimeExplorer? {
        val url = findUrl()
        if (url == null) {
            this.cancel(true)
            return null
        }
        preference.edit()
                .putString("URL", url)
                .apply()
        return findVideoUrl(url)
    }

    fun findUrl(): String? {
        if (intent.data != null)
            return intent.data.toString()
        if (intent.hasExtra(Intent.EXTRA_TEXT))
            return intent.getStringExtra(Intent.EXTRA_TEXT)
        if (preference.contains("URL"))
            return preference.getString("URL", null)
        return null
    }

    fun findVideoUrl(url: String): AnimeExplorer? {
        Log.d(MainActivity.TAG, "findVideoUrl " + url)
        return factories.firstOrNull { it.isEpisode(url) }?.episode(url)
    }

    override fun onPostExecute(result: AnimeExplorer?) {
        super.onPostExecute(result)
        Log.d(MainActivity.TAG, "onPostExecute " + result)
        if (result != null)
            function(result)
    }
}