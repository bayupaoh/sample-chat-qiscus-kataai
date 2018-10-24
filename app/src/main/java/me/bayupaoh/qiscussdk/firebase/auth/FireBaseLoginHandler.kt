package me.bayupaoh.qiscussdk.firebase.auth

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.TwitterAuthProvider
import me.bayupaoh.qiscussdk.R
import me.bayupaoh.qiscussdk.helper.CommonConstant
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterLoginButton
import org.json.JSONException
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by dodydmw19 on 7/16/18.
 */

class FireBaseLoginHandler {

    private var mAuth: FirebaseAuth? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null

    private var mCallbackManager: CallbackManager? = null

    // Init Interface
    private var googleListener: GoogleListener? = null
    private var facebookListener: FacebookListener? = null
    private var twitterListener: TwitterListener? = null

    private var twitterButton: TwitterLoginButton? = null

    companion object {
        private var sHelper: FireBaseLoginHandler? = null

        fun init(): FireBaseLoginHandler {
            if (sHelper == null) {
                sHelper = FireBaseLoginHandler()

            }
            return sHelper as FireBaseLoginHandler
        }
    }

    // Google--------------------------------------------------

    fun initializeFirebaseGoogleSignIn(context: Context?) {
        if (context != null) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context.getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()

            mGoogleSignInClient = GoogleSignIn.getClient(context, gso)
            mAuth = FirebaseAuth.getInstance()
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount, activity: Activity?) {
        if (activity != null) {
            val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
            mAuth?.signInWithCredential(credential)
                    ?.addOnCompleteListener(activity) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = mAuth?.currentUser
                            // Log.d("GOOGLE_LOG", "id : " + acct.id.toString() + " , token " + acct.idToken)
                            googleListener?.onGoogleAuthSignIn(acct.idToken.toString(), user?.uid.toString())
                        } else {
                            // If sign in fails, display a message to the user.
                            googleListener?.onGoogleAuthSignInFailed("Google Authentication Failed")
                        }
                    }
        }
    }

    fun asyncGoogle(account: GoogleSignInAccount?, activity: Activity?) {
        if (activity != null && account != null) {
            Observable.create(Observable.OnSubscribe<String> {
                val scope = "oauth2:" + Scopes.EMAIL + " " + Scopes.PROFILE
                val token = GoogleAuthUtil.getToken(activity, account.account, scope, Bundle())
                //send token to server
                googleListener?.onGoogleAuthSignIn(token.toString(), account.id)
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
        }
    }


    fun performGoogleSignIn(activity: Activity?) {
        val signInIntent = mGoogleSignInClient?.signInIntent
        activity?.startActivityForResult(signInIntent, CommonConstant.RC_SIGN_IN)
    }

    fun googleSignOut(activity: Activity?) {
        if (mAuth != null && activity != null) {
            mAuth?.signOut()
            mGoogleSignInClient?.signOut()?.addOnCompleteListener(activity) { }
        }
    }

    fun attachGoogleListener(view: GoogleListener) {
        googleListener = view
    }

    //-----------------------------------------------------

    // Facebook -------------------------------------------

    fun initializeFacebookLogin(button: LoginButton) {
        mAuth = FirebaseAuth.getInstance()
        mCallbackManager = CallbackManager.Factory.create()
        button.setReadPermissions("email", "public_profile")

        button.registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                // [START_EXCLUDE]
                facebookListener?.onFbSignInFail("Canceled")
                // [END_EXCLUDE]
            }

            override fun onError(error: FacebookException) {
                facebookListener?.onFbSignInFail("Failed Login Facebook")
            }
        })
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val request = GraphRequest.newMeRequest(
                token
        ) { _, response ->
            // Application code
            var facebookID = ""
            //  var name = ""
            //   var email: String? = ""
            try {
                facebookID = response.jsonObject.getString("id")
                // name = response.jsonObject.getString("name")
                // email = response.jsonObject.getString("email")
                //   gender = object.getString("gender");
                //    birthday = object.getString("birthday");
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            facebookListener?.onFbSignInSuccess(token.token, facebookID)
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,name,gender,first_name,last_name, email, birthday")
        request.parameters = parameters
        request.executeAsync()
    }

    fun getCallbackManager(): CallbackManager {
        return mCallbackManager!!
    }

    fun attachFacebookListener(view: FacebookListener) {
        facebookListener = view
    }

    fun facebookSignOut() {
        LoginManager.getInstance().logOut()
    }

    //------------------------------------------------------------------

    // Twitter ---------------------------------------------------------

    fun initializeTwitterLogin(button: TwitterLoginButton?, activity: Activity?) {
        if (button != null) {
            mAuth = FirebaseAuth.getInstance()
            twitterButton = button
            twitterButton?.callback = object : Callback<TwitterSession>() {
                override fun success(result: Result<TwitterSession>) {
                    val session = TwitterCore.getInstance().sessionManager.activeSession
                    val authToken = session.authToken
                    val token = authToken.token
                    val secret = authToken.secret

                    getTwitterEmail(token, secret, activity)
                }

                override fun failure(exception: TwitterException) {
                    twitterListener?.onTwitterError(exception.localizedMessage)
                }
            }
        }
    }

    private fun getTwitterEmail(token: String, secret: String, activity: Activity?) {
        if (activity != null) {
            val credential = TwitterAuthProvider.getCredential(
                    token, secret)
            mAuth?.signInWithCredential(credential)
                    ?.addOnCompleteListener(activity) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
//                        Log.e("twitter", "signInWithCredential:onComplete:" + task.isSuccessful)
//                        Log.e("twitter", "user:" + task.result.user.uid + "  :  " + task.result.user.displayName + " : "
//                                + task.result.user.email + " :  " + task.result.user.photoUrl)
                            twitterListener?.onTwitterSignIn(token, secret, task.result.user.uid)
                        } else {
                            // If sign in fails, display a message to the user.
                            twitterListener?.onTwitterError("Twitter Authentication Failed")
                            signOutTwitter()
                        }
                    }
        }
    }

    fun signOutTwitter() {
        TwitterCore.getInstance().sessionManager.clearActiveSession()
    }

    fun getTwitterActiveButton(): TwitterLoginButton {
        return twitterButton!!
    }

    fun attachTwitterListener(view: TwitterListener?) {
        twitterListener = view
    }

    // -----------------------------------------------------------------

}