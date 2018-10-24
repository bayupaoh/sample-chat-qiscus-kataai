package me.bayupaoh.qiscussdk.helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.provider.SyncStateContract.Helpers.update
import android.content.pm.PackageManager
import com.google.android.gms.common.util.ClientLibraryUtils.getPackageInfo
import android.content.pm.PackageInfo
import android.util.Base64
import android.util.Log
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


/**
 * Created by dodydmw19 on 7/18/18.
 */

class CommonUtils {

    companion object {
        @SuppressLint("PackageManagerGetSignatures")
        fun getKeyHash(context: Context) {
            try {
                val info = context.packageManager.getPackageInfo(
                        "me.bayupaoh.qiscussdk",
                        PackageManager.GET_SIGNATURES)
                for (signature in info.signatures) {
                    val md = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
                }
            } catch (e: PackageManager.NameNotFoundException) {

            } catch (e: NoSuchAlgorithmException) {

            }

        }

        fun checkTwitterApp(context: Context): Boolean {
            return try {
                var info = context.packageManager.getApplicationInfo("com.twitter.android", 0)
                Log.d("CHECK", "twitter is available")
                true
            } catch (e: PackageManager.NameNotFoundException){
                false
            }
        }

    }

}