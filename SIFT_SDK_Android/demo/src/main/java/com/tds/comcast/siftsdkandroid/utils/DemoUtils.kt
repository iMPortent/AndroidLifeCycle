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
package com.tds.comcast.siftsdkandroid.utils

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Utility class to build the Trackers and
 * to hold a static executor.
 */
class DemoUtils {

    // static scope
    companion object {

        private var executor = Executors.newSingleThreadScheduledExecutor()

        /**
         * Executes a repeating runnable
         *
         * @param runnable a new task
         * @param initDelay the delay before polling
         * @param delay the delay between polls
         * @param timeUnit the time-unit for the delays
         */
        fun scheduleRepeating(runnable: Runnable, initDelay: Long, delay: Long, timeUnit: TimeUnit) {
            executor.scheduleAtFixedRate(runnable, initDelay, delay, timeUnit)
        }

        /**
         * Shuts the executor down and resets it.
         */
        fun resetExecutor() {
            executor.shutdown()
            executor = Executors.newSingleThreadScheduledExecutor()
        }

        const val schemaString =
                "{\n" +
                "    \"\$schema\": \"http://json-schema.org/draft-04/schema\",\n" +
                "    \"type\": \"array\",\n" +
                "    \"title\": \"The root schema\",\n" +
                "    \"description\": \"The root schema comprises the entire JSON document.\",\n" +
                "    \"default\": [],\n" +
                "    \"additionalItems\": false,\n" +
                "    \"items\": {\n" +
                "        \"anyOf\": [\n" +
                "            {\n" +
                "                \"type\": \"object\",\n" +
                "                \"title\": \"The first anyOf schema\",\n" +
                "                \"description\": \"An explanation about the purpose of this instance.\",\n" +
                "                \"default\": {},\n" +
                "                \"required\": [\n" +
                "                    \"account_id\",\n" +
                "                    \"partner_id\",\n" +
                "                    \"app_name\",\n" +
                "                    \"app_ver\",\n" +
                "                    \"device_id\",\n" +
                "                    \"device_model\",\n" +
                "                    \"device_language\",\n" +
                "                    \"device_timezone\",\n" +
                "                    \"platform\",\n" +
                "                    \"os_ver\",\n" +
                "                    \"session_id\",\n" +
                "                    \"event_id\",\n" +
                "                    \"event_name\",\n" +
                "                    \"timestamp\",\n" +
                "                    \"event_schema\",\n" +
                "                    \"event_payload\"\n" +
                "                ],\n" +
                "                \"additionalProperties\": false,\n" +
                "                \"properties\": {\n" +
                "                    \"account_id\": {\n" +
                "                        \"type\": \"string\",\n" +
                "                        \"title\": \"The account_id schema\",\n" +
                "                        \"default\": \"\"\n" +
                "                    },\n" +
                "                    \"partner_id\": {\n" +
                "                        \"type\": \"string\",\n" +
                "                        \"title\": \"The partner_id schema\",\n" +
                "                        \"default\": \"\"\n" +
                "                    },\n" +
                "                    \"app_name\": {\n" +
                "                        \"type\": \"string\",\n" +
                "                        \"title\": \"The app_name schema\",\n" +
                "                        \"default\": \"\"\n" +
                "                    },\n" +
                "                    \"app_ver\": {\n" +
                "                        \"type\": \"string\",\n" +
                "                        \"title\": \"The app_ver schema\",\n" +
                "                        \"default\": \"\"\n" +
                "                    },\n" +
                "                    \"device_id\": {\n" +
                "                        \"type\": \"string\",\n" +
                "                        \"title\": \"The device_id schema\",\n" +
                "                        \"default\": \"\"\n" +
                "                    },\n" +
                "                    \"device_model\": {\n" +
                "                        \"type\": \"string\",\n" +
                "                        \"title\": \"The device_model schema\",\n" +
                "                        \"default\": \"\"\n" +
                "                    },\n" +
                "                    \"device_language\": {\n" +
                "                        \"type\": \"string\",\n" +
                "                        \"title\": \"The device_language schema\",\n" +
                "                        \"default\": \"\"\n" +
                "                    },\n" +
                "                    \"device_timezone\": {\n" +
                "                        \"type\": \"integer\",\n" +
                "                        \"title\": \"The device_timezone schema\",\n" +
                "                        \"default\": 0\n" +
                "                    },\n" +
                "                    \"platform\": {\n" +
                "                        \"type\": \"string\",\n" +
                "                        \"enum\": [\n" +
                "                            \"x1\",\n" +
                "                            \"web\",\n" +
                "                            \"ios\",\n" +
                "                            \"android\",\n" +
                "                            \"roku\",\n" +
                "                            \"coam\",\n" +
                "                            \"chromecast\",\n" +
                "                            \"sb\",\n" +
                "                            \"thortv\"\n" +
                "                        ]\n" +
                "                    },\n" +
                "                    \"os_ver\": {\n" +
                "                        \"type\": \"string\",\n" +
                "                        \"title\": \"The os_ver schema\",\n" +
                "                        \"default\": \"\"\n" +
                "                    },\n" +
                "                    \"session_id\": {\n" +
                "                        \"type\": \"string\",\n" +
                "                        \"title\": \"The session_id schema\",\n" +
                "                        \"default\": \"\"\n" +
                "                    },\n" +
                "                    \"event_id\": {\n" +
                "                        \"type\": \"string\",\n" +
                "                        \"title\": \"The event_id schema\",\n" +
                "                        \"default\": \"\"\n" +
                "                    },\n" +
                "                    \"event_name\": {\n" +
                "                        \"type\": \"string\",\n" +
                "                        \"title\": \"The event_name schema\",\n" +
                "                        \"default\": \"\"\n" +
                "                    },\n" +
                "                    \"event_type\": {\n" +
                "                        \"type\": \"string\",\n" +
                "                        \"title\": \"The event_type schema\",\n" +
                "                        \"default\": \"\"\n" +
                "                    },\n" +
                "                    \"library_ver\": {\n" +
                "                        \"type\": \"string\",\n" +
                "                        \"title\": \"The library_ver schema\",\n" +
                "                        \"default\": \"\"\n" +
                "                    },\n" +
                "                    \"timestamp\": {\n" +
                "                        \"type\": \"integer\",\n" +
                "                        \"title\": \"The timestamp schema\",\n" +
                "                        \"default\": 0\n" +
                "                    },\n" +
                "                    \"event_schema\": {\n" +
                "                        \"type\": \"string\",\n" +
                "                        \"title\": \"The event_schema schema\",\n" +
                "                        \"default\": \"\"\n" +
                "                    },\n" +
                "                    \"custom_schema\": {\n" +
                "                        \"type\": \"string\",\n" +
                "                        \"title\": \"The custom_schema schema\",\n" +
                "                        \"default\": \"\"\n" +
                "                    },\n" +
                "                    \"exo\": {\n" +
                "                        \"type\": \"object\",\n" +
                "                        \"title\": \"The exo schema\",\n" +
                "                        \"default\": {}\n" +
                "                    },\n" +
                "                    \"trace_id\": {\n" +
                "                        \"type\": \"string\",\n" +
                "                        \"title\": \"The trace_id schema\",\n" +
                "                        \"default\": \"\"\n" +
                "                    },\n" +
                "                    \"event_payload\": {\n" +
                "                        \"type\": \"object\",\n" +
                "                        \"title\": \"The event_payload schema\",\n" +
                "                        \"default\": {}\n" +
                "                    },\n" +
                "                    \"custom_payload\": {\n" +
                "                        \"type\": \"object\",\n" +
                "                        \"title\": \"The custom_payload schema\",\n" +
                "                        \"default\": {}\n" +
                "                    },\n" +
                "                    \"screen_flow\": {\n" +
                "                        \"type\": \"array\",\n" +
                "                        \"title\": \"The screen_flow schema\",\n" +
                "                        \"default\": []\n" +
                "                    },\n" +
                "                    \"breadcrumb\": {\n" +
                "                        \"type\": \"string\",\n" +
                "                        \"title\": \"The breadcrumb schema\",\n" +
                "                        \"default\": \"\"\n" +
                "                    },\n" +
                "                    \"user_id\": {\n" +
                "                        \"type\": \"string\",\n" +
                "                        \"title\": \"The user_id schema\",\n" +
                "                        \"default\": \"\"\n" +
                "                    },\n" +
                "                    \"device_type\": {\n" +
                "                        \"type\": \"string\",\n" +
                "                        \"title\": \"The device_type schema\",\n" +
                "                        \"default\": \"\"\n" +
                "                    },\n" +
                "                    \"device_make\": {\n" +
                "                        \"type\": \"string\",\n" +
                "                        \"title\": \"The device_make schema\",\n" +
                "                        \"default\": \"\"\n" +
                "                    },\n" +
                "                    \"device_name\": {\n" +
                "                        \"type\": \"string\",\n" +
                "                        \"title\": \"The device_name schema\",\n" +
                "                        \"default\": \"\"\n" +
                "                    },\n" +
                "                    \"network_type\": {\n" +
                "                        \"type\": \"string\",\n" +
                "                        \"title\": \"The network_type schema\",\n" +
                "                        \"default\": \"\"\n" +
                "                    },\n" +
                "                    \"carrier\": {\n" +
                "                        \"type\": \"string\",\n" +
                "                        \"title\": \"The carrier schema\",\n" +
                "                        \"default\": \"\"\n" +
                "                    },\n" +
                "                    \"os_type\": {\n" +
                "                        \"type\": \"string\",\n" +
                "                        \"title\": \"The os_type schema\",\n" +
                "                        \"default\": \"\"\n" +
                "                    },\n" +
                "                    \"session_count\": {\n" +
                "                        \"type\": \"integer\",\n" +
                "                        \"title\": \"The session_count schema\",\n" +
                "                        \"default\": 0\n" +
                "                    },\n" +
                "                    \"previous_session_id\": {\n" +
                "                        \"type\": \"string\",\n" +
                "                        \"title\": \"The previous_session_id schema\",\n" +
                "                        \"default\": \"\"\n" +
                "                    },\n" +
                "                    \"first_event_id\": {\n" +
                "                        \"type\": \"string\",\n" +
                "                        \"title\": \"The first_event_id schema\",\n" +
                "                        \"default\": \"\"\n" +
                "                    },\n" +
                "                    \"build\": {\n" +
                "                        \"type\": \"string\",\n" +
                "                        \"title\": \"The build schema\",\n" +
                "                        \"default\": \"\"\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}\n"
    }
}