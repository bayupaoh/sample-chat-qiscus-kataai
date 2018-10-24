package me.bayupaoh.qiscussdk.firebase.auth

/**
 * Created by dodydmw19 on 7/16/18.
 */

interface GoogleListener {

    fun onGoogleAuthSignIn(authToken: String?, userId: String?)

    fun onGoogleAuthSignInFailed(errorMessage: String?)

}