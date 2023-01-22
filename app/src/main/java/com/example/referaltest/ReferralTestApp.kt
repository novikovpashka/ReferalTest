package com.example.referaltest

import android.app.Application

class ReferralTestApp: Application() {
    override fun onCreate() {
        super.onCreate()

        val myReferral = MyReferral()
        myReferral.getReferral(this)

    }
}