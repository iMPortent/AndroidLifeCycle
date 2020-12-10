package com.siftandroidsdk.sift.tracker

import android.content.Context
import android.os.Build
import android.preference.PreferenceManager
import com.siftandroidsdk.sift.tracker.constants.Constants
import com.siftandroidsdk.sift.tracker.models.ContextConfiguration
import com.siftandroidsdk.sift.tracker.models.SiftContext
import com.siftandroidsdk.sift.tracker.util.Handler
import com.siftandroidsdk.sift.tracker.util.SiftCallbackHandler
import com.siftandroidsdk.sift.tracker.util.SiftOkHttpClient.provideFromHeaders
import com.siftandroidsdk.sift.tracker.util.SiftSQLiteStoreHelper
import com.snowplowanalytics.snowplow.tracker.*
import com.snowplowanalytics.snowplow.tracker.Tracker.instance
import com.snowplowanalytics.snowplow.tracker.constants.Parameters
import com.snowplowanalytics.snowplow.tracker.constants.TrackerConstants
import com.snowplowanalytics.snowplow.tracker.emitter.RequestCallback
import com.snowplowanalytics.snowplow.tracker.emitter.RequestSecurity
import com.snowplowanalytics.snowplow.tracker.events.SelfDescribing
import com.snowplowanalytics.snowplow.tracker.payload.SelfDescribingJson
import com.snowplowanalytics.snowplow.tracker.utils.Util

import java.util.*
import kotlin.collections.HashMap

class SiftMissingFieldsException(errorMessage: String?, err: Throwable?)
    : RuntimeException(errorMessage, err)

open class SiftSdk {

    lateinit var tracker: Tracker
    private var model = Analytics()
    private lateinit var requiredFields: RequiredFields
    private lateinit var dbHelper: SiftSQLiteStoreHelper
    private var configuration = ContextConfiguration()
    lateinit var siftContext: SiftContext
    lateinit var mContext : Context
    lateinit var mDelegate: SiftCallbackHandler


    companion object {

        var shared  = SiftSdk().apply {
            requiredFields = RequiredFields(
                    "", "","","",
                    "", "", custom_schema = "CustomSchema"
            )
        }

        private object SiftResponseCallback : RequestCallback {
            override fun onSuccess(successCount: Int) {
                shared.mDelegate.success(successCount)
            }

            override fun onFailure(successCount: Int, failureCount: Int) {
                shared.mDelegate.failure(successCount, failureCount)
            }

        }



        /**
         * Starts session checking.
         */
        fun resumeSessionChecking() {
            shared.tracker.resumeSessionChecking()
        }

        /**
         * Starts event collection processes
         * again.
         */
        fun resumeEventTracking() {
            shared.tracker.resumeEventTracking()
        }

        /**
         * Stops event collection and ends all
         * concurrent processes.
         */
        fun pauseEventTracking() {
            shared.tracker.pauseEventTracking()
        }

        /**
         * Ends session checking.
         */
        fun pauseSessionChecking() {
            shared.tracker.pauseSessionChecking()
        }

        /**
         * Convenience function for starting a new session.
         */
        fun startNewSession() {
            shared.tracker.startNewSession()
        }
    }

    // MARK: Properties Public
    var accountId: String
        get() = model.accountId
        set(value) { model.accountId = value  }

    var appName: String
        get() = model.appName
        set(value) { model.appName = value  }

    var appVer: String
        get() = model.appVer
        set(value) { model.appVer = value }

    var customSchema: String
        get() = model.customSchema
        set(value) { model.customSchema = value }

    var deviceId: String
        get() = model.deviceId
        set(value) { model.deviceId = value }

    var deviceLanguage: String
        get() = model.deviceLanguage
        set(value) { model.deviceLanguage = value }

    var deviceModel: String
        get() = model.deviceModel
        set(value) { model.deviceModel = value }

    var deviceName: String
        get() = model.deviceName
        set(value) { model.deviceName = value }

    var deviceType: String
        get() = model.deviceType
        set(value) { model.deviceType = value }

    var eventId: String
        get() = model.eventId
        set(value) { model.eventId = value }

    var eventSchema: String
        get() = model.eventSchema
        set(value) { model.eventSchema = value }

    var eventType: String
        get() = model.eventType
        set(value) { model.eventType = value }

    var exo: HashMap<String, String>
        get() = model.exo
        set(value) { model.exo = value }

    var headers: HashMap<String, String>
        get() = model.headers
        set(value) { model.headers = value }

    var customPayload: HashMap<String, Any>
        get() = model.customPayload
        set(value) { model.customPayload = value }

    var eventPayload: HashMap<String, Any>
        get() = model.eventPayload
        set(value) { model.eventPayload = value }

    var host: String
        get() = model.host
        set(value) { model.host = value }

    var osVer: String
        get() = model.osVer
        set(value) { model.osVer = value }

    var partnerId: String
        get() = model.partnerId
        set(value) { model.partnerId = value }

    var testSchema: String
        get() = model.testSchema
        set(value) { model.testSchema = value }

    var platform: String
        get() = model.platform
        set(value) { model.platform = value }

    var screenFlow: List<String>
        get() = model.screenFlow
        set(value) { model.screenFlow = value }

    var breadCrumb: String
        get() = model.breadCrumb
        set(value) {model.breadCrumb = value }

    var sessionId: String
        get() = model.sessionId
        set(value) { model.sessionId = value }

    var timestamp: Long
        get() = model.timestamp
        set(value) { model.timestamp = value }

    var contextConfiguration: ContextConfiguration
        get() = configuration
        set(value) { configuration = value }

    var defaultFields: RequiredFields
        get() = requiredFields
        set(value) {
            requiredFields.apply {
                shared.model.accountId = value.account_id
                shared.model.partnerId = value.partner_id
                shared.model.appName = value.app_name
                shared.model.appVer = value.app_ver
                shared.model.deviceId = value.device_id
                shared.model.eventName = value.event_name
                shared.model.eventSchema = value.event_schema
                shared.model.eventPayload = value.event_payload
            }
        }

    var traceId: String
        get() = model.traceId
        set(value) { model.traceId = value }

    val useHttps: Boolean
        get() = model.protocol == RequestSecurity.HTTPS

    var userId: String
        get() = model.userId
        set(value) { model.userId = value }


    // MARK: - Utilities

    fun isOnline() = Util.isOnline(mContext)
    fun isEmitterRunning() = tracker.emitter.emitterStatus
    fun currentDatabaseSize() = tracker.emitter.eventStore.size
    fun currentSessionIndex() = tracker.session.sessionIndex


    /**
     * Returns the SiftContext.
     *
     * @param context the Android context
     * @return the a SiftContext object with all the data available in SP
     */
    private fun Tracker.generateSIFTContext(context: Context): SiftContext {

        /*
            CSC Required fields:
                "account_id", "partner_id", "app_name", "app_ver", "device_id",
                "device_model", "device_language", "device_timezone", "platform",
                "os_ver", "session_id", "event_id","event_name", "timestamp",
                "event_schema", "event_payload"
        */

        // this context will hold all the data I can currently get from SP
        // and make it really easy to make them on client side when building a payload.
        val contextBlob = object {

            // grab the location.
            val location = Util.getGeoLocationContext(context)?.map

            // grab the session.
            val session = getSession()!!

            // grab the subject.
            val subject = getSubject()!!.subject
            val appName = appId

            // instantiate SharedPreferences to retrieve [ACCOUNT_ID,
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            val versionName = pInfo.versionName
            val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                pInfo.longVersionCode.toString()
            } else {
                pInfo.versionCode.toString()
            }
        }

        // maps out all available data in SDK
        // into siftContext
        return with(contextBlob) {
            SiftContext(
                    device_timezone = TimeZone.getDefault().rawOffset,
                    timestamp = location?.get(Parameters.GEO_TIMESTAMP),
                    bearing = location?.get(Parameters.BEARING),
                    altitude = location?.get(Parameters.ALTITUDE),
                    app_build = versionCode,
                    app_version = versionName,
                    app_name = appName,
                    viewport = subject[Parameters.VIEWPORT].toString(),
                    carrier = Util.getCarrier(context),
                    app_ver = sharedPreferences.getString(Parameters.APP_VERSION, versionName),
                    partner_id = sharedPreferences.getString(Constants.PARTNER_ID, "Comcast"),
                    device_make = Util.getDeviceVendor(),
                    device_model = Util.getDeviceModel(),
                    screen_id = screenState.getCurrentScreen(BuildConfig.DEBUG).map[Parameters.SCREEN_ID].toString(),
                    screen_name = screenState.getCurrentScreen(BuildConfig.DEBUG).map[Parameters.SCREEN_NAME].toString(),
                    screen_fragment = screenState.getCurrentScreen(BuildConfig.DEBUG).map[Parameters.SCREEN_FRAGMENT].toString(),
                    screen_activity = screenState.getCurrentScreen(BuildConfig.DEBUG).map[Parameters.SCREEN_ACTIVITY].toString(),
                    screen_type = screenState.getCurrentScreen(BuildConfig.DEBUG).map[Parameters.SCREEN_TYPE].toString(),
                    color_depth = subject[Parameters.COLOR_DEPTH].toString(),
                    domain_uid = subject[Parameters.DOMAIN_UID].toString(),
                    ip_address = subject[Parameters.IP_ADDRESS].toString(),
                    language = subject[Parameters.LANGUAGE].toString(),
                    latlong_accuracy = location?.get(Parameters.LATLONG_ACCURACY),
                    longitude = location?.get(Parameters.LONGITUDE),
                    latitude = location?.get(Parameters.LATITUDE),
                    namespace = namespace,
                    network_technology = Util.getNetworkTechnology(Util.getNetworkInfo(context)),
                    network_type = Util.getNetworkType(Util.getNetworkInfo(context)),
                    network_uuid = subject[Parameters.NETWORK_UID],
                    os_type = Util.getOsType(),
                    user_id = Util.getUUIDString(),
                    os_ver = Util.getOsVersion(),
                    platform = platform.value,
                    library_ver = trackerVersion,
                    first_event_id = (session.sessionValues["firstEventId"]  ?: "").toString(),
                    session_first_id = (session.sessionValues["sessionId"]  ?: "").toString(),
                    session_id = (session.sessionValues["userId"]  ?: "").toString(),
                    session_index = session.sessionValues["sessionIndex"] as Int,
                    session_previous_index = (session.sessionValues["previousSessionId"]  ?: "").toString(),
                    session_storage = (session.sessionValues["storageMechanism"]  ?: "").toString(),
            )
        }
    }

    private fun getTracker(url: String, protocol: RequestSecurity) : Tracker {
        val network = OkHttpNetworkConnection
                .OkHttpNetworkConnectionBuilder(url)
                .callback(Handler(dbHelper))
                .emitTimeout(10000)
                .security(protocol)
                .client(provideFromHeaders(headers))
                .build()

        // Create the Emitter object responsible for sending events.
        // note: EmitterBuilder has to be initialized with the application context. This will
        // ensure that you don't accidentally leak an Activity's context.
        val emitter = Emitter.EmitterBuilder(
                url,
                mContext
        ).apply {
            networkConnection(network)
            callback(SiftResponseCallback)
        }.build()

        // (optional) to provide extra information along with the event
        val subject = Subject.SubjectBuilder()
                .context(mContext)
                .build()

        // construct the tracker object
        val builder = Tracker.TrackerBuilder(
                emitter,
                "namespace",
                appName,
                mContext
        )

        // initialize it
        Tracker.init(builder.apply {
            base64(false)
            platform(DevicePlatforms.Desktop)
            subject(subject)
            loggerDelegate(Handler(dbHelper))
            threadCount(20)
            sessionContext(configuration.sessionContext)
            mobileContext(configuration.mobileContext)
            geoLocationContext(configuration.geoLocationContext)
            applicationCrash(configuration.applicationCrash)
            trackerDiagnostic(configuration.trackerDiagnostic)
            lifecycleEvents(configuration.lifecycleEvents)
            foregroundTimeout(60)
            backgroundTimeout(30)
            screenContext(configuration.screenViewEvents)
            installTracking(configuration.installTracking)
            applicationContext(configuration.applicationContext)
        }.build())

        tracker = instance()

        siftContext = tracker.generateSIFTContext(mContext)

        return tracker
    }

    /**
     *  This sets the server and protocol
     *  @param host: The http server to send the event
     *  @param delegate: Either http or https
     *  @param context: The context of the application.
     *
     *  @throws: hostAlreadySet exception if called more than once
     * */
    fun init(context: Context, delegate: SiftCallbackHandler, host: String) {
        mContext = context
        print("setting host")
        model.host = host
        mDelegate = delegate
        dbHelper = SiftSQLiteStoreHelper(mContext)
        tracker = getTracker(model.host, model.protocol)
        model.protocol = RequestSecurity.HTTPS
        return
    }


    fun tagEvent(eventName: String, customPayload: HashMap<String, Any>? = null, eventPayload: HashMap<String, Any>? = null) {
        val posterSchema = TrackerConstants.SCHEMA_PAYLOAD_DATA

        // map the payloads to the model (for convenience)
        model.customPayload = customPayload ?: hashMapOf()
        model.eventPayload = eventPayload ?: hashMapOf()

        val siftContext = tracker.generateSIFTContext(mContext)

        val dataMap = hashMapOf<String, Any>()

        dataMap["account_id"] = model.accountId
        dataMap["app_name"] = model.appName
        dataMap["app_ver"] = model.appVer
        dataMap["carrier"] = siftContext.carrier as String
        dataMap["custom_payload"] = customPayload as HashMap<String, Any>
        dataMap["custom_schema"] = model.customSchema
        dataMap["device_id"] = model.deviceId
        dataMap["device_language"] = model.deviceLanguage
        dataMap["device_make"] = model.deviceMake
        dataMap["device_model"] = model.deviceModel
        dataMap["device_name"] = model.deviceName
        dataMap["device_type"] = model.deviceType
        dataMap["device_timezone"] = siftContext.device_timezone
        dataMap["event_id"] = model.eventId
        if (eventName.isNotEmpty())
            dataMap["event_schema"] = model.eventSchema
        dataMap["event_payload"] = eventPayload as HashMap<String, Any>
        dataMap["event_type"] = model.eventType
        dataMap["exo"] = model.exo
        dataMap["os_ver"] = model.osVer
        dataMap["partner_id"] = model.partnerId
        dataMap["platform"] = model.platform
        dataMap["screen_flow"] = model.screenFlow
        dataMap["session_id"] = model.sessionId
        dataMap["timestamp"] = model.timestamp
        dataMap["trace_id"] = model.traceId
        dataMap["user_id"] = model.userId
        dataMap["breadcrumb"] = model.breadCrumb

        val sdj = SelfDescribingJson(posterSchema, dataMap)

        val event = SelfDescribing.builder().eventData(sdj).build()

        tracker.track(event) // queue, then send events
    }

}// SiftSdk


// private model class to map CSC fields
private data class Analytics(
        var accountId: String = "",
        var appName: String = "",
        var appVer: String = "",
        var customSchema: String = "",
        var deviceId: String = "",
        var deviceLanguage: String = "",
        var deviceModel: String = "",
        var deviceName: String = "",
        var deviceType: String = "",
        var eventId: String = "",
        var eventSchema: String = "",
        var eventType: String = "",
        var exo: HashMap<String, String> = hashMapOf(),
        var eventPayload: HashMap<String, Any> = hashMapOf(),
        var customPayload: HashMap<String, Any> = hashMapOf(),
        var headers: HashMap<String, String> = hashMapOf(),
        var host: String = "",
        var osVer: String = "",
        var partnerId: String = "",
        var testSchema: String = "",
        var platform: String = "android",
        var eventName: String = "",
        var protocol: RequestSecurity = RequestSecurity.HTTPS,
        var screenFlow: List<String> = listOf(),
        var breadCrumb: String = "",
        var sessionId: String = "",
        var timestamp: Long = System.currentTimeMillis(),
        var traceId: String = "",
        var userId: String = "",
        var deviceMake: String = ""
)


data class RequiredFields(
        val account_id: String,
        val partner_id: String,
        val app_name: String,
        val app_ver: String,
        val device_id: String,
        val platform: String,
        val event_name: String = "",
        val event_schema: String = "",
        val event_payload: HashMap<String, Any> = hashMapOf(),
        val custom_schema: String= ""
)


