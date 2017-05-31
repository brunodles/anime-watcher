package brunodles.animewatcher

import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import brunodles.animacurse.AnimaCurseFactory
import brunodles.animesproject.AnimesProjectFactory
import brunodles.animewatcher.databinding.ActivityMainBinding
import brunodles.anitubex.AnitubexFactory
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = "MainActivity"
    }

    var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        CheckUrl(intent) {
            binding?.text?.text = it
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(it))
            startActivity(intent)
        }.execute()
    }

    private class CheckUrl(private val intent: Intent, private val function: (String) -> Unit) : AsyncTask<Void, Void, String?>() {

        val factories by lazy { Arrays.asList(AnitubexFactory, AnimaCurseFactory, AnimesProjectFactory) }

        override fun doInBackground(vararg params: Void?): String? {
            val url = findUrl()
            if (url == null) {
                this.cancel(true)
                return null
            }
            return findVideoUrl(url)
        }

        fun findUrl(): String? {
            if (intent.data != null)
                return intent.data.toString()
            else if (intent.hasExtra(Intent.EXTRA_TEXT))
                return intent.getStringExtra(Intent.EXTRA_TEXT)
            return null
        }


        fun findVideoUrl(url: String): String? {
            Log.d(TAG, "findVideoUrl " + url)
            for (factory in factories) {
                if (factory.isEpisode(url))
                    return factory.episode(url).currentEpisode().video
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Log.d(TAG, "onPostExecute " + result)
            if (result != null)
                function(result)
        }
    }
}
