package com.wynneplaga.beyondfilm

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.core.animation.doOnStart
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.wynneplaga.beyondfilm.databinding.ActivitySignInBinding

class SignInActivity : BeyondFilmActivity() {

    private lateinit var binding: ActivitySignInBinding
    private val TAG = "SignInActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        launchInitialAnimations(binding)

        binding.signInButton.setOnClickListener(this::logInWithGoogle)
    }

    private fun logInWithGoogle(v: View?) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        val client = GoogleSignIn.getClient(this, gso)
        val requestId = listenForActivityResult { resultCode, data ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithCredential:success")
                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME
                                startActivity(intent)
                                finish()
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithCredential:failure", task.exception)
                                Snackbar.make(v!!, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                            }
                        }
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                Snackbar.make(v!!, "Failed to sign in with google", Snackbar.LENGTH_LONG).show()
            }
        }
        startActivityForResult(client.signInIntent, requestId)
    }

    @SuppressLint("Recycle")
    private fun launchInitialAnimations(binding: ActivitySignInBinding) = binding.materialCardView.onFirstLayout {
        AnimationSet(
                ObjectAnimator.ofFloat(0f, 1f).apply {
                    addUpdateListener { binding.welcomeImage.alpha = it.animatedValue as Float }
                },
                ObjectAnimator.ofFloat(
                        binding.topText.translationX - 10f.dpToPx(resources.displayMetrics),
                        binding.topText.translationX
                ).apply {
                    addUpdateListener {
                        binding.topText.alpha = it.animatedFraction
                        binding.topText.translationX = it.animatedValue as Float
                    }
                },
                ObjectAnimator.ofFloat(
                        binding.bottomText.translationX - 10f.dpToPx(resources.displayMetrics),
                        binding.bottomText.translationX
                ).apply {
                    addUpdateListener {
                        binding.bottomText.alpha = it.animatedFraction
                        binding.bottomText.translationX = it.animatedValue as Float
                    }
                },
                binding.materialCardView.let {
                    ObjectAnimator.ofFloat(it.translationY + it.height.toFloat(), it.translationY).apply {
                        addUpdateListener { anim ->
                            it.translationY = anim.animatedValue as Float
                        }
                        doOnStart { _ -> it.alpha = 1f }
                        interpolator = OvershootInterpolator()
                        duration = 1000
                    }
                },
                defaultDuration = 500
        ).start()
    }

}