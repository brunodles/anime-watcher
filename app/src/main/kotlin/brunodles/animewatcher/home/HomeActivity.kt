package brunodles.animewatcher.home

import android.content.Intent
import android.content.res.Resources
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.support.v7.graphics.Target
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import brunodles.adapter.EmptyAdapter
import brunodles.animewatcher.ImageLoader
import brunodles.animewatcher.R
import brunodles.animewatcher.databinding.ActivityHomeBinding
import brunodles.animewatcher.databinding.ItemEpisodeBinding
import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.persistence.Firebase
import brunodles.animewatcher.persistence.Preferences
import brunodles.animewatcher.player.PlayerActivity
import brunodles.rxfirebase.singleObservable
import brunodles.rxpicasso.asSingle
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit


class HomeActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    companion object {
        val TAG = "HomeActivity"
        val RC_SIGN_IN = 1
        val UNKNOWN_EPISODE = Episode("", 0, "")
    }

    private lateinit var binding: ActivityHomeBinding

    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var auth: FirebaseAuth

    private var homeAdapter: FirebaseRecyclerAdapter<String, HomeAdapter.EpisodeHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        //        val layoutManager = GridLayoutManager(this, resources.getInteger(R.integer.home_columns), GridLayoutManager.VERTICAL, true)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        layoutManager.stackFromEnd = true
        binding.history.layoutManager = layoutManager
        binding.history.adapter = EmptyAdapter()
        binding.signInButton.setOnClickListener {
            startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient), RC_SIGN_IN)
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        auth = FirebaseAuth.getInstance()

        setImage(Single.just(Preferences(this).getLastAnimeImage())
                .subscribeOn(Schedulers.io()))
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    override fun onStop() {
        super.onStop()
        homeAdapter?.stopListening()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleSignInResult(result)
        }
    }

    private fun handleSignInResult(result: GoogleSignInResult) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess)
        if (result.isSuccess) {
            // Signed in successfully, show authenticated UI.
            val acct = result.signInAccount
            firebaseAuthWithGoogle(acct!!)
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(null)
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        //                            Toast.makeText(this@GoogleSignInActivity, "Authentication failed.",
                        //                                    Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user == null) {
            binding.signInButton.visibility = View.VISIBLE
            return
        }
        binding.signInButton.visibility = View.GONE

        //        homeAdapter.setUser(user)

        val options = FirebaseRecyclerOptions.Builder<String>()
                .setQuery(Firebase.history(user)
                        .limitToLast(100)
                        .orderByKey(), String::class.java)
                .build()
        homeAdapter = object :
                FirebaseRecyclerAdapter<String, HomeAdapter.EpisodeHolder>(options) {
            override fun onBindViewHolder(holder: HomeAdapter.EpisodeHolder?, position: Int, model: String?) {
                holder?.onBind(UNKNOWN_EPISODE)
                model?.let { link ->
                    Firebase.videoRef(link)
                            .singleObservable(Episode::class.java)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeBy(
                                    onSuccess = { holder?.onBind(it) },
                                    onError = { Log.e(TAG, "onBindViewHolder: ", it) }
                            )
                }
                holder?.clickListener = ::onItemClick
            }

            override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HomeAdapter.EpisodeHolder
                    = HomeAdapter.EpisodeHolder(ItemEpisodeBinding.inflate(
                    LayoutInflater.from(parent?.context), parent, false))
        }
        binding.history.adapter = homeAdapter
        //        binding.history.adapter = EmptyStateAdapterDecorator(homeAdapter, object :
        //                EmptyStateAdapterDecorator.BindingProvider<HomeAdapter.EmptyHolder> {
        //            override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HomeAdapter.EmptyHolder
        //                    = HomeAdapter.EmptyHolder(ItemEmptyBinding.inflate(
        //                    LayoutInflater.from(parent?.context), parent, false))
        //
        //            override fun onBindViewHolder(holder: HomeAdapter.EmptyHolder?, position: Int) {
        //            }
        //        })
        homeAdapter?.startListening()


        updateAnimeImage(user)
    }

    private fun updateAnimeImage(user: FirebaseUser) {
        val image = Firebase.lastOnHistory(user)
                .subscribeOn(Schedulers.io())
                .flatMap { Firebase.videoRef(it).singleObservable(Episode::class.java) }
                .flatMapObservable { ImageLoader.searchObservable("Wallpaper ${it.animeName}") }
                .map { it.size(800, 600) }
                .map { it.listImageUrls() }
                .doOnNext {
                    Observable.fromIterable(it)
                            .subscribeOn(Schedulers.io())
                            .subscribeBy(onNext = { ImageLoader.fetch(this, it) })
                }
                .map { it.firsts(5) }
                .map { it.random() }
                .singleOrError()
                .timeout(30, TimeUnit.SECONDS)
                .doOnSuccess { Preferences(this).setLastAnimeImage(it) }
                .doOnError {
                    if (it.message?.contains("decode stream") == true)
                        updateAnimeImage(user)
                }
        setImage(image)
    }

    private fun setImage(single: Single<String>) =
            single.flatMap {
                ImageLoader.picasso(this)
                        .load(it)
                        .asSingle()
            }.observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess {
                        Palette.from(it).generate {
                            it.vibrantSwatch?.let {
                                binding.mainCollapsing.setContentScrimColor(it.rgb)
                                binding.coordinator.setStatusBarBackgroundColor(it.rgb)
                                binding.mainAppbar.setBackgroundColor(it.rgb)
                                binding.mainCollapsing.setExpandedTitleColor(it.titleTextColor)

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                                    window.statusBarColor = it.rgb
                            }
                        }
                    }
                    .subscribeBy(
                            onSuccess = { binding.mainBackdrop.setImageBitmap(it) },
                            onError = {
                                Log.e(TAG, "setImage: setImage", it)
                            }
                    )

    fun onItemClick(episode: Episode) {
        startActivity(PlayerActivity.newIntent(this, episode))
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

private fun <E> List<E>.firsts(max: Int): List<E> {
    val lastIndex = if (this.size >= max) max else this.size
    return this.subList(0, lastIndex)
}

private fun <E> List<E>.random(): E = this[Random().nextInt(this.size)]
