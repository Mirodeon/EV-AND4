package com.mirodeon.ev_and4.app

import android.app.Application
import com.mirodeon.ev_and4.db.AppDatabase

class MyApp : Application() {

    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: MyApp
            private set
    }
}