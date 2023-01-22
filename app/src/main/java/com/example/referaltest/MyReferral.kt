package com.example.referaltest

import android.content.Context
import android.net.Uri
import android.util.Log
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class MyReferral {

    private lateinit var referrerClient: InstallReferrerClient

    fun getReferral(context: Context) {

        referrerClient = InstallReferrerClient.newBuilder(context).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {

            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        // Connection established.
                        val response: ReferrerDetails = referrerClient.installReferrer
                        val referrerUrl = response.installReferrer
                        Log.v("referrer", parseUtm(referrerUrl).toString())
                    }
                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                        // API not available on the current Play Store app.
                    }
                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                        // Connection couldn't be established.
                    }
                }
            }

            override fun onInstallReferrerServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }

    fun parseUtm(uriString: String): HashMap<String, String> {

        val query = Uri.parse(uriString).query
        val utmMap: HashMap<String, String> = HashMap()
        query?.let {
            val queryString = query.removeRange(0, query.indexOf("utm"))
            for (s in queryString.split("&")) {
                if (s.startsWith("utm")) {
                    val utmPair = s.split("=")
                    utmMap[utmPair[0]] = URLDecoder.decode(utmPair[1], StandardCharsets.UTF_8.toString()).trim()
                }
            }
        }
        return utmMap
    }
}