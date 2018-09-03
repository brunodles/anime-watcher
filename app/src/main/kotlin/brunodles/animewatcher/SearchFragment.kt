package brunodles.animewatcher

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import brunodles.animewatcher.databinding.FragmentSearchBinding
import java.net.URLEncoder

class SearchFragment : Fragment() {

    companion object {
        private const val TAG = "SearchFragment"
        private const val GOOGLE_SEARCH = "http://google.com/search?q="
    }

    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.searchText.setOnEditorActionListener { v, actionId, event ->
            val query = binding.searchText.text.toString()
            startActivity(Intent(ACTION_VIEW).setData(Uri.parse(query)))
            return@setOnEditorActionListener true
        }
    }

    private fun buildUrl(query: String) = GOOGLE_SEARCH + URLEncoder.encode(
        "$query ${BuildConfig.GOGOLE_QUERY}",
        "UTF-8"
    )
}