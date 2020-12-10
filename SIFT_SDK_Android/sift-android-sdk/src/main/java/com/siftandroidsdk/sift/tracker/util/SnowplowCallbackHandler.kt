package com.siftandroidsdk.sift.tracker.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import com.siftandroidsdk.sift.tracker.SiftMissingFieldsException
import com.siftandroidsdk.sift.tracker.SiftSdk.Companion.shared
import com.snowplowanalytics.snowplow.tracker.LoggerDelegate
import com.snowplowanalytics.snowplow.tracker.emitter.RequestCallback
import com.snowplowanalytics.snowplow.tracker.emitter.SiftCallback
import com.snowplowanalytics.snowplow.tracker.utils.Logger
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

interface SnowplowCallbackHandler : LoggerDelegate, RequestCallback

class Handler(private val dbHelper: SiftSQLiteStoreHelper) : SiftCallback, SnowplowCallbackHandler{

    private var JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()

    override fun getHeaders(): Map<String, String> {
        return shared.headers
    }
    @Suppress("UNCHECKED_CAST")
    override fun getDictionary(payload: MutableMap<Any?, Any?>?): RequestBody {

        val array = arrayListOf<Any>()

        val eventSchema: String = if (payload?.get("schema") != null ) {
            payload["schema"] as String
        } else {
            return Gson().toJson(array).toRequestBody(JSON)
        }
        println("------------- dictionary ------------")
        println(payload as Any)
        println("-------------------------------------")

        val payloads: ArrayList<Any> = payload["data"] as ArrayList<Any>

        payloads.forEach {
            val payloadToSend = getDictionaryToSend(it as HashMap<String, Any>, eventSchema)
            array.add(payloadToSend)
        }

        val myJsonSchema: String = shared.testSchema
        val myJsonData = GsonBuilder()
                .setPrettyPrinting()
                .create()
                .toJson(array)

        var isValidJson = false

        try{
            if (validateEvent(jsonSchema = myJsonSchema, jsonData = myJsonData)){
                isValidJson = true
                return GsonBuilder()
                        .setPrettyPrinting()
                        .create()
                        .toJson(array).toRequestBody()
            }

        } catch (err: RuntimeException) {
            if (!isValidJson) {
                dbHelper.removeAllEvents()
                Logger.d("SnowplowCallbackHandler: removed events. Current db size", dbHelper.eventStore.size.toString())
                throw SiftMissingFieldsException(
                        "You are missing required fields, check your schema. ", err)

                // we've somehow received an invalid JSON
                // so we need to purge it from the DB to prevent
                // sub-sequential request to fail.

            }
        }

        return GsonBuilder()
                .setPrettyPrinting()
                .create()
                .toJson(array).toRequestBody()
    }

    @Suppress("UNCHECKED_CAST")
    private fun getDictionaryToSend(data: HashMap<String, Any>, eventSchema: String): HashMap<String, Any> {

        // converting the string representation of co and ue_pr into a useful data structure
        val newData: HashMap<String, Any> = siftConverter(data)

        // todo: return the new payload after mapping it out from the globalContextMap
        return hashMapOf<String, Any>().apply {
            this["account_id"] = shared.accountId
            this["app_name"] = newData["aid"] as String
            this["app_ver"] = newData["version"] as String
            this["breadcrumb"] = ""
            this["build"] = shared.siftContext.app_build as String
            this["carrier"] = shared.siftContext.carrier as String
            // note: the custom payload could potentially be parsed out from the ue
            this["custom_schema"] = shared.siftContext.custom_schema as String
            this["device_id"] = shared.deviceId as String
            this["device_language"] = shared.siftContext.device_language as String
            this["device_make"] = newData["deviceManufacturer"] as String
            this["device_model"] = shared.siftContext.device_model as String
            this["device_name"] = shared.siftContext.device_name as String
            this["device_timezone"] = shared.siftContext.device_timezone
            this["device_type"] = shared.siftContext.device_type as String
            this["event_id"] = data["eid"] as String
            this["event_name"] = shared.siftContext.event_name as String
            this["event_schema"] = shared.eventSchema
            // if the payload is not created by the user, make one essentially...
            if ((newData["event_payload"] as LinkedTreeMap<String, Any>).size < 4){
                this["event_payload"] = newData["event_payload"] as LinkedTreeMap<String, Any>
            } else {
                this["event_payload"] = shared.eventPayload
            }
            this["custom_payload"] = shared.customPayload
            this["event_type"] = shared.siftContext.event_type as String
            this["exo"] = shared.exo
            this["library_ver"] = shared.siftContext.library_ver as String// ConnectedLiving-AdditionField
            this["network_type"] = shared.siftContext.network_type as String
            this["os_type"] = shared.siftContext.os_type as String // CCS
            this["os_ver"] = shared.siftContext.os_ver as String
            this["partner_id"] = shared.partnerId
            this["platform"] = shared.platform
            this["previous_session_id"] = newData["previousSessionId"] ?: "none"
//            this["screen_flow"] = newData["name"]
            this["session_count"] = (newData["sessionIndex"] as Double).toInt()
            this["first_event_id"] = newData["firstEventId"] ?: shared.tracker.session.firstId
            this["session_id"] = shared.tracker.session.currentSessionId ?: ""
            this["timestamp"] =  java.lang.Long.valueOf(data["dtm"].toString())
            this["trace_id"] = shared.traceId
            this["user_id"] = shared.userId

        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun siftConverter(data: HashMap<String, Any>) : HashMap<String, Any> {

        //todo: deprecate SiftContext and use these structures (Maps)
        // below instead.


        val finalContextList = arrayListOf<LinkedTreeMap<String, Any>>()

        // capturing and converting the co string to a HashMap<String, Any>
        val unstructuredEventMap: Map<String, Any> = Gson().fromJson(
                data["ue_pr"].toString(),
                object : TypeToken<HashMap<String, Any>>() {
                    // do nothing
                }.type
        )

        // capture and cast the root-level 'data' map
        val dataOneUE = unstructuredEventMap["data"] as LinkedTreeMap<String, Any>
        val schemaUE = dataOneUE["schema"] as String
        val dataTwoUE = dataOneUE["data"] as LinkedTreeMap<String, Any>

        print("unstructuredEvent [dataTwoUE]: $dataTwoUE")

        // capturing and converting the ue_pr string to a HashMap<String, Any>
        val globalContextMap: Map<String, Any> = Gson().fromJson(
                data["co"].toString(),
                object : TypeToken<HashMap<String, Any>>() {
                    // do nothing
                }.type
        )

        // capture and cast the root-level 'data' map
        val dataOneCO = globalContextMap["data"] as ArrayList<LinkedTreeMap<String, Any>>
        val schemaCO = globalContextMap["schema"] as String

        dataOneCO.forEach { context ->
            context["data"]?.let { finalContextList.add(it as LinkedTreeMap<String, Any>) }
        }

        // rebuilding it
        finalContextList.forEach{
            data.putAll(it)
        }

        // installEvents, backgroundEvent and foreGroundEvent
        // these get wrapped into the event_payload.
        // map the unstructured event. In the case of installEvent
        // background/foreground events we'll wrap the contents into
        // the event_payload field.
//        if (!dataTwoUE.containsKey("event_payload")) {
            data["event_payload"] = dataTwoUE
//        }

        print("finalContextList: $data")

        return data
    }

    /**
     * Returns the Emitter Logger Callback.
     */
    override fun error(p0: String, p1: String) {
        shared.mDelegate.e(p0, p1)
    }
    override fun debug(p0: String, p1: String) {
        shared.mDelegate.d(p0, p1)
    }
    override fun verbose(p0: String, p1: String) {
        shared.mDelegate.v(p0, p1)
    }

    /**
     * Returns the Emitter Request Callback.
     */
    override fun onSuccess(successCount: Int) = shared.mDelegate.success(successCount)
    override fun onFailure(successCount: Int, failureCount: Int) = shared.mDelegate.failure(successCount, failureCount)
}

interface SiftCallbackHandler {
    fun e(tag: String, msg: String)
    fun d(tag: String, msg: String)
    fun v(tag: String, msg: String)
    fun success(successCount: Int)
    fun failure(successCount: Int, failureCount: Int)
}
