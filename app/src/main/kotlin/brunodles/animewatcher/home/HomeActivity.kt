package brunodles.animewatcher.home

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import brunodles.animewatcher.R
import brunodles.animewatcher.databinding.ActivityHomeBinding
import brunodles.animewatcher.persistence.Firebase
import brunodles.animewatcher.player.PlayerActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class HomeActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    companion object {
        val TAG = "Companion"
        val RC_SIGN_IN = 1
    }

    private lateinit var binding: ActivityHomeBinding
    private lateinit var homeAdapter: HomeAdapter

    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.history.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, true)
        homeAdapter = HomeAdapter()
        homeAdapter.onPageExplorerClickListener = { episode ->
            startActivity(PlayerActivity.newIntent(this, episode))
        }
        homeAdapter.onLinkClickListener = { link ->
            startActivity(PlayerActivity.newIntent(this, link))
        }
        homeAdapter.onLoginClickListener = {
            startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient), RC_SIGN_IN)
        }
        homeAdapter.add(LoginRequest())
//        Preferences(this).getUrl()?.let { homeAdapter.add(it) }
        binding.recyclerView.adapter = homeAdapter

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
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
            firebaseAuthWithGoogle(acct!!)
        } else {
//            homeAdapter.addLoginRequest()
            // Signed out, show unauthenticated UI.
            updateUI(null)
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        val user = auth.getCurrentUser()
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException())
                        //                            Toast.makeText(this@GoogleSignInActivity, "Authentication failed.",
                        //                                    Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user == null) {
//            homeAdapter.addLoginRequest
            return
        }
        homeAdapter.removeLogin()

//        val ref = Firebase.history(user)
//        val adapter = object : FirebaseRecyclerAdapter<String, RecyclerView.ViewHolder>(
//                String::class.java, R.layout.item_unknown, RecyclerView.ViewHolder::class.java, ref) {
//            override fun populateViewHolder(viewHolder: RecyclerView.ViewHolder?, model: String?, position: Int) {
//                val text = viewHolder?.itemView?.findViewById<TextView>(R.id.text)
//                text?.text = model
//            }
//        }
//        binding.history.setAdapter(adapter)

        Firebase.history(user).addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                p0?.let { homeAdapter.add(it.value as String) }
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}