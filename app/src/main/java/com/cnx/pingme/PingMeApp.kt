package com.cnx.pingme

import android.app.Activity
import android.app.Application
import com.cnx.pingme.dependencyInjection.AppInjector
import com.facebook.stetho.Stetho
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject


class PingMeApp  : Application() , HasActivityInjector {

    @Inject
    public lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>


    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Stetho.initializeWithDefaults(this)

        AppInjector.init(this)

    }

    override fun activityInjector() = dispatchingAndroidInjector


}