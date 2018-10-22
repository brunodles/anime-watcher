package brunodles.animewatcher.search

import android.annotation.TargetApi
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import brunodles.animewatcher.R
import brunodles.animewatcher.databinding.FragmentSearchBinding
import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.player.PlayerActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy

class SearchFragment : Fragment() {

    companion object {
        private const val TAG = "SearchFragment"
    }

    private lateinit var binding: FragmentSearchBinding
    private val searchController: SearchController by lazy { SearchController(context!!) }
    private val disposables = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_search, container, false
        )
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
                    return searchController.onUrl(
                        url,
                        playerStarter = ::startPlayer,
                        urlLoader = ::loadUrl
                    )
                }
            }
        }
    }

    private fun startPlayer(episode: Episode) {
        startActivity(PlayerActivity.newIntent(context!!, episode))
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
        val autoCompleteAdapter = AutoCompleteAdapter(context!!)
        disposables += searchController.searchHistory()
            .doOnSubscribe { autoCompleteAdapter.clear() }
            .subscribeBy(
                onNext = {
                    Log.d(TAG, "onStart: searchHistory.onNext: ${it.element}")
                    autoCompleteAdapter.add(it.element)
                }
            )
        binding.searchText.setAdapter(autoCompleteAdapter)
    }

    override fun onStop() {
        super.onStop()
        binding.searchText.setOnEditorActionListener(null)
        binding.searchButton.setOnClickListener(null)
        disposables.dispose()
    }

    fun search() {
        val query = binding.searchText.text.toString()
        binding.webview.loadUrl(searchController.buildUrl(query))
        searchController.addSearch(query)
//        startActivity(Intent(ACTION_VIEW).setData(Uri.parse(buildUrl(query))))
    }
}