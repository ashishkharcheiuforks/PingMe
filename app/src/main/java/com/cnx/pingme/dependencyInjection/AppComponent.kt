package com.cnx.pingme.dependencyInjection

import android.app.Application
import com.cnx.pingme.PingMeApp
import com.cnx.pingme.worker.SendMsgWorker
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class, ActivityModule::class]
)
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(application: PingMeApp)

    fun injectIntoWorker(worker: SendMsgWorker)
}
