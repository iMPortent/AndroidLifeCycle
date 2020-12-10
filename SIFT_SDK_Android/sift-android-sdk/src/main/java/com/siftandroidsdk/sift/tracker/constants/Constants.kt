package com.siftandroidsdk.sift.tracker.constants

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

/**
 * All of the keys for each type of event
 * that can be sent by the Tracker.
 */
object Constants {
    // General
    const val EVENT_SCHEMA = "event_schema"
    const val PAYLOAD = "data"
    const val EVENT_NAME = "event_name"
    const val EVENT_ID = "event_id"

    @Deprecated("")
    const val TIMESTAMP = "timestamp"
    const val DEVICE_TIMESTAMP = "device_timestamp"
    const val LIBRARY_VER = "library_ver"
    const val APP_NAME = "app_name"
    const val NAMESPACE = "namespace"
    const val USER_UID = "user_id"
    const val CONTEXT = "custom_object"
    const val CONTEXT_ENCODED = "cx"
    const val EVENT_PAYLOAD = "event_payload"
    const val CUSTOM_PAYLOAD = "custom_payload"
    const val CUSTOM_SCHEMA = "custom_schema"
    const val UNSTRUCTURED = "ue_pr"
    const val UNSTRUCTURED_ENCODED = "ue_px"

    // Subject class
    const val PLATFORM = "platform"
    const val RESOLUTION = "resolution"
    const val VIEWPORT = "viewport"
    const val COLOR_DEPTH = "color_depth"
    const val TIMEZONE = "device_timezone"
    const val LANGUAGE = "device_language"
    const val IP_ADDRESS = "ip_address"
    const val USERAGENT = "user_agent"
    const val NETWORK_UID = "network_uuid"
    const val DOMAIN_UID = "domain_uid"

    // Unstructured Event
    const val ACCOUNT_ID = "account_id"
    const val PARTNER_ID = "partner_id"
    const val APP_VER = "app_ver"
    const val DEVICE_ID = "device_id"

    // Screen View
    const val SV_ID = "id"
    const val SV_NAME = "name"
    const val SV_TYPE = "type"
    const val SV_PREVIOUS_NAME = "previous_name"
    const val SV_PREVIOUS_ID = "previous_dd"
    const val SV_PREVIOUS_TYPE = "previous_type"
    const val SV_TRANSITION_TYPE = "transition_type"

    // Mobile context
    const val ANDROID_IDFA = "android_idfa"
    const val CARRIER = "carrier"
    const val DEVICE_MODEL = "device_model"
    const val DEVICE_MAKE = "device_make"
    const val OS_VERSION = "os_ver"
    const val OS_TYPE = "os_type"
    const val NETWORK_TYPE = "network_type"
    const val NETWORK_TECHNOLOGY = "network_technology"

    // Geolocation context
    const val LATITUDE = "latitude"
    const val LONGITUDE = "longitude"
    const val ALTITUDE = "altitude"
    const val LATLONG_ACCURACY = "latlong_accuracy"
    const val SPEED = "speed"
    const val BEARING = "bearing"
    const val GEO_TIMESTAMP = "timestamp"

    // Session Context
    const val SESSION_USER_ID = "user_id"
    const val SESSION_ID = "session_id"
    const val SESSION_PREVIOUS_ID = "previous_session_id"
    const val SESSION_INDEX = "session_count"
    const val SESSION_STORAGE = "session_storage"
    const val SESSION_FIRST_ID = "first_event_id"

    // Screen Context
    const val SCREEN_NAME = "name"
    const val SCREEN_ID = "id"
    const val SCREEN_TYPE = "type"
    const val SCREEN_FRAGMENT = "fragment"
    const val SCREEN_ACTIVITY = "activity"
    const val SCREEN_FLOW = "screen_flow"

    // Application Context
    const val APP_VERSION = "app_ver"
    const val APP_BUILD = "build"

    // Application Crash
    const val APP_ERROR_MESSAGE = "message"
    const val APP_ERROR_STACK = "stack_trace"
    const val APP_ERROR_THREAD_NAME = "thread_name"
    const val APP_ERROR_THREAD_ID = "thread_id"
    const val APP_ERROR_LANG = "programming_language"
    const val APP_ERROR_LINE = "line_number"
    const val APP_ERROR_CLASS_NAME = "class_name"
    const val APP_ERROR_EXCEPTION_NAME = "exception_name"
    const val APP_ERROR_FATAL = "is_fatal"

    // Application Focus
    const val APP_FOREGROUND_INDEX = "foreground_index"
    const val APP_BACKGROUND_INDEX = "background_index"

    // Tracker Diagnostic
    const val DIAGNOSTIC_ERROR_MESSAGE = "message"
    const val DIAGNOSTIC_ERROR_STACK = "stack_trace"
    const val DIAGNOSTIC_ERROR_CLASS_NAME = "class_name"
    const val DIAGNOSTIC_ERROR_EXCEPTION_NAME = "exception_name"

    // GDPR
    const val BASIS_FOR_PROCESSING = "basisForProcessing"
    const val DOCUMENT_ID = "documentId"
    const val DOCUMENT_VERSION = "documentVersion"
    const val DOCUMENT_DESCRIPTION = "documentDescription"
}