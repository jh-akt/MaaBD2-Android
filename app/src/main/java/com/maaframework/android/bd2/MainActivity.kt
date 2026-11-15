package com.maaframework.android.bd2

import com.maaframework.android.ui.MaaHostActivity

class MainActivity : MaaHostActivity() {
    override val hostVersionName: String = BuildConfig.VERSION_NAME
    override val hostPreferencesName: String = "maabd2_android_settings"
}
