package brunodles.animewatcher.home

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import brunodles.animewatcher.R
import brunodles.animewatcher.databinding.ActivityHomeBinding
import brunodles.animewatcher.persistence.Preferences
import brunodles.animewatcher.player.PlayerActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient


class HomeActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    companion object {
        val TAG = "Companion"
        val RC_SIGN_IN = 1
    }

    private lateinit var binding: ActivityHomeBinding
    private lateinit var homeAdapter: HomeAdapter

    private lateinit var mGoogleApiClient: GoogleApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        homeAdapter = HomeAdapter()
        homeAdapter.onAnimeExplorerClickListener = { episode ->
            startActivity(PlayerActivity.newIntent(this, episode))
        }
        homeAdapter.onLinkClickListener = { link ->
            startActivity(PlayerActivity.newIntent(this, link))
        }
        homeAdapter.onLoginClickListener = {
            startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient), RC_SIGN_IN)
        }
        homeAdapter.add(LoginRequest())
        Preferences(this).getUrl()?.let { homeAdapter.add(it) }
        binding.recyclerView.adapter = homeAdapter

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
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
            homeAdapter.removeLogin()
        } else {
//            homeAdapter.addLoginRequest()
            // Signed out, show unauthenticated UI.
//            updateUI(false)
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}