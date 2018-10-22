package brunodles.animewatcher

import android.annotation.TargetApi
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import brunodles.animewatcher.databinding.FragmentSearchBinding
import brunodles.animewatcher.player.EpisodeController
import brunodles.animewatcher.player.PlayerActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import java.net.URLEncoder

class SearchFragment : Fragment() {

    companion object {
        private const val TAG = "SearchFragment"
        private const val GOOGLE_SEARCH = "http://google.com/search?q="
    }

    private lateinit var binding: FragmentSearchBinding
    private val episodeController: EpisodeController by lazy { EpisodeController(context!!) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        setup(binding.webview)
        return binding.root
    }

    private fun setup(webview: WebView) {
        with(webview) {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {

                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    return shouldOverrideUrl(request?.url?.toString())
                }

                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    return shouldOverrideUrl(url)
                }

                private fun shouldOverrideUrl(url: String?): Boolean {
                    Log.d(TAG, "shouldOverrideUrl: url: $url")
                    if (url == null)
                        return false
                    if (url.contains("google.com")) {
                        Log.d(TAG, "shouldOverrideUrl: containsGoogle, using default")
                        return false
                    }

                    episodeController.findVideoOn(url)
                        .toMaybe()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                            onSuccess = {
                                Log.d(TAG, "shouldOverrideUrl: start player for url: $url")
                                startActivity(PlayerActivity.newIntent(context, it))
                            },
                            onError = {
                                Log.d(TAG, "shouldOverrideUrl: webView load url: $url")
                                Snackbar.make(
                                    binding.searchText,
                                    "Failed to decode this page.",
                                    Snackbar.LENGTH_SHORT
                                ).setAction("open") {
                                    loadUrl(url)
                                }.show()
                            }
                        )
                    return true
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.searchText.setOnEditorActionListener { v, actionId, event ->
            search()
            return@setOnEditorActionListener true
        }
        binding.searchButton.setOnClickListener { v ->
            search()
        }
    }

    fun search() {
        val query = binding.searchText.text.toString()
        binding.webview.loadUrl(buildUrl(query))
//        startActivity(Intent(ACTION_VIEW).setData(Uri.parse(buildUrl(query))))
    }

    private fun buildUrl(query: String) = GOOGLE_SEARCH + URLEncoder.encode(
        "$query ${BuildConfig.GOGOLE_QUERY}",
        "UTF-8"
    )
}