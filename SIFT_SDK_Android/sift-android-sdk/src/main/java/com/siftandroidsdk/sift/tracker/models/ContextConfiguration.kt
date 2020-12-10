package com.siftandroidsdk.sift.tracker.models


/**
 * All standard contexts are enabled by Default.
 * Can be overridden during tracker initialization.
 * */
data class ContextConfiguration(
        var geoLocationContext: Boolean = true,
        val mobileContext: Boolean = true,
        val applicationCrash: Boolean = true,
        val trackerDiagnostic: Boolean = true,
        val lifecycleEvents: Boolean = true,
        val screenViewEvents: Boolean = true,
        val screenContext: Boolean = true,
        val installTracker : Boolean= true,
        val installTracking: Boolean = true,
        val sessionContext: Boolean = true,
        val applicationContext: Boolean = true
)
