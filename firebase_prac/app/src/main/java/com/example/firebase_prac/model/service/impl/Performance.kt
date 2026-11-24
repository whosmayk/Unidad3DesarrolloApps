package com.example.firebase_prac.model.service.impl

import com.google.firebase.perf.FirebasePerformance

inline fun <T> trace(name: String, block: () -> T): T {
    val trace = FirebasePerformance.getInstance().newTrace(name)
    trace.start()
    return try {
        block()
    } finally {
        trace.stop()
    }
}
