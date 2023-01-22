package com.mobven.shortly.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.mobven.shortly.utils.Constants
import javax.inject.Inject
import javax.inject.Singleton

interface AnalyticsManager {
    fun shortenClickEvent(success: Boolean)
    fun shareClickEvent(position: Int)
    fun deleteClickEvent(position: Int)
    fun copyClickEvent(position: Int)
    fun getStartedScreenEvent()
    fun linkHistoryScreenEvent(count: Int)
    fun themeTypeAppEvent(themeMode: String)
}

@Singleton
class AnalyticsManagerImpl @Inject constructor(private val firebaseAnalytics: FirebaseAnalytics) :
    AnalyticsManager {
    override fun shortenClickEvent(success: Boolean) {
        val bundle = Bundle().apply {
            putBoolean(Constants.AnalyticsParam.SUCCESS,success)
        }
        firebaseAnalytics.logEvent(Constants.AnalyticsEvent.SHORTEN,bundle)
    }

    override fun shareClickEvent(position: Int) {
        firebaseAnalytics.logEvent(Constants.AnalyticsEvent.SHARE){
            param(Constants.AnalyticsParam.POSITION,position.toLong())

        }
    }

    override fun deleteClickEvent(position: Int) {
        firebaseAnalytics.logEvent(Constants.AnalyticsEvent.DELETE){
            param(Constants.AnalyticsParam.POSITION,position.toLong())

        }
    }

    override fun copyClickEvent(position: Int) {
        firebaseAnalytics.logEvent(Constants.AnalyticsEvent.COPY){
            param(Constants.AnalyticsParam.COUNT,position.toLong())
        }
    }

    override fun getStartedScreenEvent() {
        firebaseAnalytics.logEvent(Constants.AnalyticsEvent.GET_STARTED,null)
    }

    override fun linkHistoryScreenEvent(count: Int) {
        firebaseAnalytics.logEvent(Constants.AnalyticsEvent.LINK_HISTORY){
            param(Constants.AnalyticsParam.COUNT,count.toLong())
        }
    }

    override fun themeTypeAppEvent(themeMode: String) {
        firebaseAnalytics.logEvent(themeMode,null)
    }
}