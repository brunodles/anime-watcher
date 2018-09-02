package brunodles.animewatcher.history

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import brunodles.adapter.EmptyAdapter
import brunodles.animewatcher.R
import brunodles.animewatcher.databinding.ItemEpisodeBinding
import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.home.HomeActivity
import brunodles.animewatcher.persistence.Firebase
import brunodles.animewatcher.player.PlayerActivity
import brunodles.rxfirebase.singleObservable
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class HistoryFragment : Fragment() {

    private var adapter: FirebaseRecyclerAdapter<String, HistoryAdapter.EpisodeHolder>? = null
    private lateinit var recyclerView: RecyclerView
    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recyclerView = inflater.inflate(R.layout.fragment_episode, container, false) as RecyclerView

        val layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, true)
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = EmptyAdapter()
        return recyclerView
    }

    override fun onStart() {
        super.onStart()
        adapter = FirebaseAuth.getInstance().currentUser?.let {
            val adapter = Adapter(it, disposable, ::onItemClick)
            recyclerView.adapter = adapter
            adapter.startListening()
            adapter
        }
    }

    private fun onItemClick(episode: Episode) {
        context?.let {
            startActivity(PlayerActivity.newIntent(it, episode))
        }
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    class Adapter(
        currentUser: FirebaseUser,
        private val disposable: CompositeDisposable,
        private val onItemClick: (Episode) -> Unit
    ) :
        FirebaseRecyclerAdapter<String, HistoryAdapter.EpisodeHolder>(
            FirebaseRecyclerOptions.Builder<String>()
                .setQuery(
                    Firebase.history(currentUser)
                        .limitToLast(100)
                        .orderByKey(), String::class.java
                )
                .build()
        ) {

        override fun onBindViewHolder(
            holder: HistoryAdapter.EpisodeHolder,
            position: Int,
            model: String
        ) {
//            holder.onBind(HomeActivity.UNKNOWN_EPISODE)
            model.let { link ->
                disposable.add(
                    Firebase.videoRef(link)
                        .singleObservable(Episode::class.java)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                            onSuccess = { holder.onBind(it) },
                            onError = { Log.e(HomeActivity.TAG, "onBindViewHolder: ", it) }
                        ))
            }
            holder.clickListener = onItemClick
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): HistoryAdapter.EpisodeHolder = HistoryAdapter.EpisodeHolder(
            ItemEpisodeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
}