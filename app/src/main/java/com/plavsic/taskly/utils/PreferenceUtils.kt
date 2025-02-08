package com.plavsic.taskly.utils

import android.content.Context

object PreferenceUtils {

    private const val PREF_NAME = "app_preferences"
    private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"

    fun saveOnboardingStatus(context: Context, isCompleted: Boolean) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(KEY_ONBOARDING_COMPLETED, isCompleted).apply()
    }

    fun hasUserCompletedOnboarding(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(KEY_ONBOARDING_COMPLETED, false)
    }

}