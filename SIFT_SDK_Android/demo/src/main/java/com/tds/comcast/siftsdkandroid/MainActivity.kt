/*
 * Copyright (c) 2015-2020 Snowplow Analytics Ltd. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package com.tds.comcast.siftsdkandroid

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.provider.Settings
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.siftandroidsdk.sift.tracker.RequiredFields
import com.siftandroidsdk.sift.tracker.SiftSdk
import com.siftandroidsdk.sift.tracker.models.ContextConfiguration
import com.siftandroidsdk.sift.tracker.util.SiftCallbackHandler
import com.tds.comcast.siftsdkandroid.utils.DemoUtils
import com.tds.comcast.siftsdkandroid.utils.DemoUtils.Companion.schemaString
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.function.Consumer


/**
 * Classic Demo Activity.
 */
class MainActivity : AppCompatActivity(), SiftCallbackHandler {

    private var eventPayload = hashMapOf<String, Any>()
    private var customPayload = hashMapOf<String, Any>()

    private var eventsCreated = 0
    private var eventsSent = 0
    private lateinit var callbackIsPermissionGranted: Consumer<Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // conditionally enabling strict mode
        // if not running in DEBUG mode
        if (!BuildConfig.DEBUG){
            enableStrictMode()
        }

        // read in whatever we had previously saved and update the UI with it.
        readIDsFromPrefs()

        // configuring and initializing the tracker
        SiftSdk.shared.apply {

            // initializing required fields
            defaultFields = RequiredFields(
                    account_id = account_id.text.toString(),
                    partner_id = partner_id.text.toString(),
                    app_ver = app_ver_id.text.toString(),
                    device_id = device_id.text.toString(),
                    app_name = this@MainActivity.getString(R.string.app_name),
                    platform = "android"
            )

            // overriding any tracker context configuration as needed
            contextConfiguration = ContextConfiguration(
                    screenContext = false
            )

            // setting the schema so we can perform
            // validation with the Json data
            testSchema = schemaString

            // setting custom headers
            headers = hashMapOf<String, String>().apply {
                this["x-api-key"] = "qRp7pmTEpu1uXplt71VwG4U4MUzNFlra47kaoz2b"
            }

            // setting global properties
            customSchema = "CustomSchema"
            deviceId = Settings.Secure.ANDROID_ID
            exo = hashMapOf<String, String>().apply {
                this["exoText"] = "Testing 4"
            }
            screenFlow = mutableListOf<String>().apply {
                this.add("screen1")
                this.add("screen2")
            }

            // initializing the tracker
            init(
                    context = applicationContext,
                    delegate = this@MainActivity,
                    host = "9kbz3z5nrj.execute-api.us-east-1.amazonaws.com/beta/apps"
            )
        }

        // creating a custom payload
        customPayload.apply {
            this["receiver_id"] = "3a8080a0-4e46-4d68-adca-b7424b4c998d"
            this["device_mac"] = "12:BF:60:cd:93:5B"
            this["subsession_id"] = "9efadcde-4aa0-410b-a5af-4d03995de209"
            this["subsession_type"] = "Browse"
        }

        eventPayload = hashMapOf(
                Pair("page_title", "Free to Me"),
                Pair("page_type", "Browse"),
                Pair("page_menu_id", ""),
                Pair("page_node_id", ""),
                Pair("page_entity_type", "menu"),
                Pair("page_url", "/browse/123"),
                Pair("target_menu_id", ""),
                Pair("target_node_id", ""),
                Pair("target_position", 0),
                Pair("target_type", "tile"),
                Pair("target_title", "Shrek"),
                Pair("target_entity_type", "movie"),
                Pair("target_entity_id", ""),
                Pair("target_url", "/entity/movie/456"),
                Pair("parent_menu_id", ""),
                Pair("parent_node_id", ""),
                Pair("parent_position", 5),
                Pair("parent_type", "row"),
                Pair("parent_title", "Popular Kids"),
                Pair("parent_entity_type", "menu"),
                Pair("parent_entity_id", ""),
                Pair("action", "enter"),
                Pair("breadcrumb", "Flex Home | Free to Me"),
                Pair("search_phrase", "Shrek"),
        )

        // Setup Listeners
        setupTrackerListener()
        setupTabListener()

        log_output.movementMethod = ScrollingMovementMethod()
        log_output.text = ""
    }

    private fun readIDsFromPrefs() {
        applicationContext.getSharedPreferences("MY_SHARED_PREFS", MODE_PRIVATE).apply {
            emitter_uri_field.setText(
                    getString("emitter_uri", "9kbz3z5nrj.execute-api.us-east-1.amazonaws.com/beta/apps")
            )
            account_id.setText(
                    getString("account_id", "")
            )
            partner_id.setText(
                    getString("partner_id", "Comcast")
            )
            app_ver_id.setText(
                    getString("app_ver_id", BuildConfig.VERSION_NAME)
            )
            device_id.setText(
                    getString("device_id", Settings.Secure.ANDROID_ID)
            )
        }
    }

    private fun enableStrictMode() {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDialog()
                .build())
        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().detectAll()
                .penaltyLog()
                .build())
    }

    override fun onDestroy() {
        DemoUtils.resetExecutor()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        SiftSdk.resumeSessionChecking()
    }

    /**
     * Setups listener for tabs.
     */
    private fun setupTabListener() {
        btn_lite_tab.setOnClickListener {
            SiftSdk.resumeSessionChecking()
            val url = "https://snowplowanalytics.com/"
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(this@MainActivity, Uri.parse(url))
        }
    }

    /**
     * Builds and sets up the Tracker listener for the demo.
     */
    private fun setupTrackerListener() {

        makePollingUpdater()

        radio_data_collection.setOnCheckedChangeListener { _, i ->
            if (i == R.id.radio_data_on) {
                SiftSdk.resumeEventTracking()
            } else if (i == R.id.radio_data_off) {
                SiftSdk.pauseEventTracking()
            }
        }
        btn_lite_start.setOnClickListener {

            // save the user input to file
            saveUserPrefs()

            if (Build.VERSION.SDK_INT < 24) {
                created_events.text = eventsCreated++.toString()

                // track the event
                SiftSdk.shared.tagEvent("custom_event", customPayload, eventPayload)
            }
            // setupTrackingEvents if ACCESS_FINE_LOCATION has been granted.
            requestPermissions { if (it)
                created_events.text = eventsCreated++.toString()

                // track the event
                SiftSdk.shared.tagEvent("custom_event", customPayload, eventPayload)
            }
        }
    }

    private fun saveUserPrefs() {
        /** Save the event tracking config to SharedPreferences. */
        applicationContext.getSharedPreferences("MY_SHARED_PREFS", MODE_PRIVATE).apply {
            with(this.edit()) {
                putString("app_ver_id", if (app_ver_id.text.toString() != "")
                    app_ver_id.text.toString()
                else "").apply()
                putString("emitter_uri", if (emitter_uri_field.text.toString() != "")
                    emitter_uri_field.text.toString()
                else "").apply()
                putString("account_id", if (account_id.text.toString().isNotEmpty())
                    account_id.text.toString()
                else "").apply()
                putString("partner_id", if (partner_id.text.toString() != "")
                    partner_id.text.toString()
                else "").apply()
                putString("device_id", if (device_id.text.toString() != "")
                    device_id.text.toString()
                else "").apply()
            }
        }
    }

    @TargetApi(24)
    private fun requestPermissions(callbackIsGranted: Consumer<Boolean>) {
        callbackIsPermissionGranted = callbackIsGranted
        val permissionState = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionState == PackageManager.PERMISSION_GRANTED) {
            callbackIsGranted.accept(true)
            return
        }
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        ActivityCompat.requestPermissions(this, permissions, APP_PERMISSION_REQUEST_LOCATION)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (Build.VERSION.SDK_INT < 24) {
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == APP_PERMISSION_REQUEST_LOCATION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            callbackIsPermissionGranted.accept(true)
            return
        }
        callbackIsPermissionGranted.accept(false)
    }

    /**
     * Updates the logger with a message.
     *
     * @param message the message to add to the log.
     */
    private fun updateLogger(message: String) : Unit = runOnUiThread { log_output.append(message) }


    /**
     * Updates the events sent counter.
     *
     * @param count the amount of successful events
     */
    @SuppressLint("SetTextI18n")
    private fun updateEventsSent(count: Int) : Unit = runOnUiThread {
        eventsSent += count
        sent_events.text = "Sent: $eventsSent"
    }

    /**
     * Updates the various UI elements based on information
     * about the Tracker and Emitter.
     *
     * @param isOnline is the device online
     * @param isRunning is the emitter running
     * @param dbSize the database event size
     */
    private fun updateEmitterStats(
            isOnline: Boolean,
            isRunning: Boolean,
            dbSize: Long,
            sessionIndex: Int,
            userId: String,
    ) = runOnUiThread {
        online_status.text = if (isOnline) "Online: yes" else "Online: no"
        emitter_status.text = if (isRunning) "Running: yes" else "Running: no"
        val dbSizeStr = "DB Size: $dbSize"
        database_size.text = dbSizeStr

        val sessionIndexStr = "Session #: $sessionIndex"
        session_index.text = sessionIndexStr

        val userIdStr = "User Id #: $userId"
        user_id.text = userIdStr

        if (isRunning) {
            when (btn_lite_start.text.toString()) {
                "Start!", "Running  ." -> btn_lite_start.setText(R.string.running_1)
                "Running.  " -> btn_lite_start.setText(R.string.running_2)
                else -> btn_lite_start.setText(R.string.running_3)
            }
        } else btn_lite_start.setText(R.string.start)
    }

    /**
     * Starts a polling updater which will fetch
     * and update the UI.
     */
    private fun makePollingUpdater() {
        DemoUtils.scheduleRepeating({
            updateEmitterStats(
                    isOnline = SiftSdk.shared.isOnline(),
                    isRunning = SiftSdk.shared.isEmitterRunning(),
                    dbSize = SiftSdk.shared.currentDatabaseSize(),
                    sessionIndex = SiftSdk.shared.currentSessionIndex(),
                    userId = SiftSdk.shared.userId,
            )
        }, initDelay = 1, delay = 1, timeUnit = TimeUnit.SECONDS)
    }

    override fun e(tag: String, msg: String) {
        Log.d("[$tag]", msg)
    }

    override fun d(tag: String, msg: String) {
        Log.d("[$tag]", msg)
    }

    override fun v(tag: String, msg: String) {
        Log.d("[$tag]", msg)
    }

    override fun success(successCount: Int) {
        val message = "Emitter succeeded:\n" +
                "- Events sent: $successCount "
        runOnUiThread{ Toast.makeText(this, message, Toast.LENGTH_LONG).show() }
        updateLogger(message)
        updateEventsSent(successCount)
    }

    override fun failure(successCount: Int, failureCount: Int) {
        val message = "Emitter failed: \n" +
                "- Events sent: $successCount \n" +
                "- Events failed: $failureCount"
        runOnUiThread{ Toast.makeText(this, message, Toast.LENGTH_LONG).show() }
        updateLogger(message)
    }
}

const val APP_PERMISSION_REQUEST_LOCATION = 1

// --- Tracker
const val namespace = "SIFT SDK Android Demo"
const val appId = "TDS-ComcastID"
