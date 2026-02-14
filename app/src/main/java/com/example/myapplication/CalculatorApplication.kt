package com.example.simplecalculator

import android.app.Application
import com.google.android.material.color.DynamicColors

class CalculatorApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Apply dynamic colors to all activities
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}