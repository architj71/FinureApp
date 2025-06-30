package com.example.finure

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class to initialize Hilt dependency injection.
 * Annotated with @HiltAndroidApp to trigger code generation.
 */
@HiltAndroidApp
class FinureApp : Application()
