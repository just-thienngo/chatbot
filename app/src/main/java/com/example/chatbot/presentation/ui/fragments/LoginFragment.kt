package com.example.chatbot.presentation.ui.fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.chatbot.R
import com.example.chatbot.databinding.FragmentLoginBinding
import com.example.chatbot.presentation.ui.activities.HomeChatActivity
import com.example.chatbot.presentation.viewmodels.LoginViewModel
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private val RC_SIGN_IN = 9001
    private val RC_GITHUB_SIGN_IN = 9002
    private lateinit var googleSignInClient: GoogleSignInClient
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var callbackManager: CallbackManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the callbackManager
        callbackManager = CallbackManager.Factory.create()

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        // Set up Facebook Login button
        binding.imgFacebook.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email", "public_profile"))
            LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    // Authentication successful, proceed to the home screen
                    loginViewModel.firebaseAuthWithFacebook(loginResult.accessToken)
                }

                override fun onCancel() {
                    Toast.makeText(context, "Facebook login canceled.", Toast.LENGTH_SHORT).show()
                }

                override fun onError(exception: FacebookException) {
                    Toast.makeText(context, "Facebook login error: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // Sign-up button navigation
        binding.tvSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        // Google Sign-In button
        binding.imgGoogle.setOnClickListener {
            signInWithGoogle()
        }

        binding.imgGithub.setOnClickListener {
            signInWithGithub()

        }

        // Observe login result
        loginViewModel.loginResult.observe(viewLifecycleOwner, Observer { isSuccess ->
            if (isSuccess) {
                Toast.makeText(context, "Authentication successful.", Toast.LENGTH_SHORT).show()
                Intent(requireActivity(), HomeChatActivity::class.java).also { intent ->
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun signInWithGithub() {
        val provider = OAuthProvider.newBuilder("github.com")
        val pendingResultTask = FirebaseAuth.getInstance().pendingAuthResult
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                .addOnSuccessListener { authResult ->
                    // User is signed in.
                    val credential = authResult.credential as OAuthCredential
                    val accessToken = credential.accessToken
                    accessToken?.let {
                        loginViewModel.signInWithGithub(it)
                    } ?: run {
                        Toast.makeText(context, "GitHub access token is null", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    // Handle failure.
                    if (e.message?.contains("The web operation was canceled by the user") == true) {
                        Toast.makeText(context, "GitHub sign-in canceled by user.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "GitHub sign-in failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            // There's no pending result so you need to start the sign-in flow.
            FirebaseAuth.getInstance()
                .startActivityForSignInWithProvider(requireActivity(), provider.build())
                .addOnSuccessListener { authResult ->
                    // User is signed in.
                    val credential = authResult.credential as OAuthCredential
                    val accessToken = credential.accessToken
                    accessToken?.let {
                        loginViewModel.signInWithGithub(it)
                    } ?: run {
                        Toast.makeText(context, "GitHub access token is null", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    // Handle failure.
                    if (e.message?.contains("The web operation was canceled by the user") == true) {
                        Toast.makeText(context, "GitHub sign-in canceled by user.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "GitHub sign-in failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }



    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    // Handle the Facebook and Google sign-in results
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Handle Facebook login result
        callbackManager.onActivityResult(requestCode, resultCode, data)

        // Handle Google login result
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    loginViewModel.firebaseAuthWithGoogle(account)
                }
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign-in failed: ${e.statusCode}")
                Toast.makeText(context, "Google sign-in failed: ${e.statusCode}", Toast.LENGTH_SHORT).show()
            }
        }
        // Handle GitHub login result
        if (requestCode == RC_GITHUB_SIGN_IN) {
            val uri = data?.data
            if (uri != null && uri.toString().startsWith("https://chatbot-cbf32.firebaseapp.com/__/auth/handler")) {
                val code = uri.getQueryParameter("code")
                if (code != null) {
                    // Exchange the code for an access token
                    exchangeGithubCodeForToken(code)
                }
            }
        }
    }

    private fun exchangeGithubCodeForToken(code: String) {

    }
}
