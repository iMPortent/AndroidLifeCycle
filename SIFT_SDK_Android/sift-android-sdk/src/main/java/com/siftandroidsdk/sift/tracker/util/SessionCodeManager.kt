package com.siftandroidsdk.sift.tracker.util

class SessionCodeManager {

    var siftSessionCode: SiftSessionCode = SiftSessionCode()
    var sessionCodeTrail: SessionCodeTrail

    init {
        sessionCodeTrail = SessionCodeTrail(siftSessionCode.getUniqueSessionCode())
    }

    fun addEvent(sessionCode: String, event: String){
        sessionCodeTrail.addSessionEvent(sessionCode, event)
    }

    fun getCodeTrail(): SessionCodeTrail {
        return sessionCodeTrail.getSessionCodeTrail()
    }

    fun getSessionCode(): String{
        return siftSessionCode.getUniqueSessionCode()
    }

}