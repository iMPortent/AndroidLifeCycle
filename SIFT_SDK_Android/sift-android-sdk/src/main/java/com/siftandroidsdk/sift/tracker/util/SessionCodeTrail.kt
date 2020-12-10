package com.siftandroidsdk.sift.tracker.util

class SessionCodeTrail constructor(sessionCode: String){

    private var sessionCode: String = ""
    private var sessionCodeMap: Map<String, ArrayList<String>> = mutableMapOf()

    init {
        this.sessionCode = sessionCode
    }

    fun addSessionEvent(sessionCode: String, sessionEvent: String){
        if(sessionCode == this.sessionCode && !sessionCodeMap.get(sessionCode).isNullOrEmpty()){
            sessionCodeMap[sessionCode]?.add(sessionEvent)
        }
    }

    fun getSessionCodeTrail(): SessionCodeTrail{
        return this
    }

    fun checkThisEventList(sessionCode: String): ArrayList<String>?{
        if(sessionCodeMap.isNotEmpty()){
            return sessionCodeMap[sessionCode]
        }
        return null
    }

    private fun removeEventFromList(sessionCode: String, event: String){
        if((sessionCodeMap[sessionCode] ?: error("NA")).contains(event)){
            sessionCodeMap[sessionCode]?.remove(event)
        }
    }


}