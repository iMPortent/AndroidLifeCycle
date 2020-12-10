package com.siftandroidsdk.sift.tracker.util

import android.util.Log
import android.view.View

class SessionCodeAttacher: View.OnClickListener {

    var sessionCodeManager: SessionCodeManager = SessionCodeManager()
    var sessionCodeTrail: SessionCodeTrail = sessionCodeManager.getCodeTrail()

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
        sessionCodeManager.addEvent(sessionCodeManager.getSessionCode(), p0.toString())
        Log.d("TAG", sessionCodeTrail.checkThisEventList(sessionCodeManager.getSessionCode()).toString())
    }

}