# SIFT_SDK_Android

## Integration Guide
​
The guide, with installation usage instructions and examples,
[can currently be found on Google Docs](https://docs.google.com/document/d/1HX3f82WJ7uuN9gbCfbBqcwZyVOIfGsxOTU-OuZz8N9M/).
​
## Installation
​
You can easily begin integrating the SDK into your project by adding it as a dependency to your Gradle:

- [app-level gradle]      
  
       implementation "com.comcast.siftandroidsdk:sift-android-sdk:1.0.0@aar" 
      
- [module-level-gradle] 

       maven {
          url "https://nexus.comcast.com/nexus/content/repositories/hosted-pabs-releases"
       }
        
## Usage
​
For now, please refer to the 
[integration guide](https://docs.google.com/document/d/1cJSzx_5iXzljGPsMm2TeyG69sslmO-3kp43zT8ego3c/) for
usage instructions, API info, and examples. This repository will soon be updated with further reference
material, but for the time being the integration guide is the best source of information.
​
## Code Samples (from Demo app) 
# Configuring and initializing the tracker

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
# Setting a schema for validation (v3/v4 draft only)
       // set the string representation of the schema
       // to be used in validation against the Json data
       SiftSdk.shared.testSchema = schemaString
# Creating an event and custom payload
        eventPayload = hashMapOf<String, Any>().apply {
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
# Tagging an event
        SiftSdk.shared.tagEvent("custom_event", customPayload, eventPayload)
​
## Support
​
For support and feedback on the initial release, you can reach out at any time on Slack at `#sift-android-sdk-support`.

