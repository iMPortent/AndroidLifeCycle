package com.siftandroidsdk.sift.tracker.util

import com.fasterxml.jackson.databind.JsonNode
import com.github.fge.jackson.JsonLoader
import com.github.fge.jsonschema.core.report.ProcessingMessage
import com.github.fge.jsonschema.core.report.ProcessingReport
import com.github.fge.jsonschema.main.JsonSchema
import com.github.fge.jsonschema.main.JsonSchemaFactory
import com.google.common.collect.Lists
import java.util.*
import kotlin.collections.ArrayList


/**
 *
 * This Util function can be used to validate your JSON string
 * against the schema of your choosing (v3-v4 Draft supported)
 *
 * */
@Throws(Exception::class)
fun validateEvent(jsonData: String, jsonSchema: String): Boolean {
    // create the Json nodes for schema and data
    val schemaNode: JsonNode = JsonLoader.fromString(jsonSchema) // throws JsonProcessingException if error
    val data: JsonNode = JsonLoader.fromString(jsonData) // same here
    val factory: JsonSchemaFactory = JsonSchemaFactory.byDefault()
    // load the schema and validate
    val schema: JsonSchema = factory.getJsonSchema(schemaNode)
    val report: ProcessingReport = schema.validate(data)

    ((report)).forEach {
        println(it.message)
        println(it)
    }



    println("The payload: $jsonData " +
            "\n was validated? - ${
                (report.isSuccess)
                        .toString()
                        .toUpperCase(Locale.ROOT)
            }"
    )
    return report.isSuccess
}