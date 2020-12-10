package com.siftandroidsdk.sift.tracker.util

import java.util.*

/**
 * Generates a random number that will be linked to users login session
 */

class SiftSessionCode {

    /**
     * field to hold the session code
     */
    private var uniqueSessionCode: String = ""

    /**
     * checks if a session code currently exists
     */
    private fun hasSessionCode(): Boolean {
        return uniqueSessionCode.isNotEmpty()
    }

    /**
     * generates a random session code
     */
    private fun generateUSC(): String {
        return UUID.randomUUID().toString()
    }

    /**
     * where the unique code will be set
     */
    private fun setUniqueSessionCode(uniqueSessionCode: String) {
        this.uniqueSessionCode = uniqueSessionCode
    }

    /**
     * retrieve current unique code
     */
    fun getUniqueSessionCode(): String{
        return if(!hasSessionCode()){ newSessionCode() } else uniqueSessionCode
    }

    /**
     * generates a code and sets it to the field
     */
    private fun newSessionCode(): String{
        setUniqueSessionCode(generateUSC())
        return uniqueSessionCode
    }

    /**
     * upon initialization of the class, set a new session code
     */
    init {
        if(!hasSessionCode()){
            newSessionCode()
        }
    }

}