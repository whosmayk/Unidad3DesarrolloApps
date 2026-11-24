package com.example.firebase_prac.model.service.impl

import com.example.firebase_prac.model.service.LogService
import com.google.firebase.crashlytics.FirebaseCrashlytics

class LogServiceImpl : LogService {
    override fun logNonFatal(throwable: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(throwable)
    }

    override fun logCrash(throwable: Throwable) {
        // Crashlytics logs automatic unhandled crashes
    }
}
