package com.example.firebase_prac.model.service

interface LogService {
    fun logNonFatal(throwable: Throwable)
    fun logCrash(throwable: Throwable)
}
