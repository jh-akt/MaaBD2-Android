package com.maaframework.android.bd2

import android.app.Application
import com.maaframework.android.MaaFrameworkAndroid

class MaaBd2App : Application() {
    override fun onCreate() {
        super.onCreate()
        MaaFrameworkAndroid.initialize(this)
    }
}
