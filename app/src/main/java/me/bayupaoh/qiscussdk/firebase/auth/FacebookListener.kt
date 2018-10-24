package me.bayupaoh.qiscussdk.firebase.auth

/**
 * Created by dodydmw19 on 7/16/18.
 */

interface FacebookListener {

    fun onFbSignInFail(errorMessage: String?)

    fun onFbSignInSuccess(authToken: String?, userId: String?)

}