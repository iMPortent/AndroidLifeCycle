package com.siftandroidsdk.sift.tracker.util

import android.content.Context
import com.siftandroidsdk.sift.tracker.SiftSdk
import com.snowplowanalytics.snowplow.tracker.emitter.EmitterEvent
import com.snowplowanalytics.snowplow.tracker.payload.Payload
import com.snowplowanalytics.snowplow.tracker.storage.EventStore
import com.snowplowanalytics.snowplow.tracker.storage.SQLiteEventStore

interface SiftStoreHelper : EventStore

class SiftSQLiteStoreHelper(context: Context) : SiftStoreHelper{

    var eventStore: SQLiteEventStore = SQLiteEventStore(context)

    override fun add(payload: Payload) {
        TODO("Not yet implemented")
    }

    /**
     * Removes an event from the store.
     * @param id the identifier of the event in the store.
     * @return a boolean of success to remove.
     */
    override fun removeEvent(id: Long): Boolean {
        if (eventStore.isDatabaseOpen) {
            eventStore.removeEvent(id)
        }
        return false
    }

    /**
     * Removes a range of events from the store.
     * @param ids the events' identifiers in the store.
     * @return a boolean of success to remove.
     */
    override fun removeEvents(ids: MutableList<Long>): Boolean {
        if (ids.size == 0) {
            return false
        }
        while (!eventStore.isDatabaseOpen) {
            Thread.sleep(300)
        }
        eventStore.removeEvents(ids)

        SiftSdk.shared.mDelegate.v("SiftSQLiteEventStoreHelper", "Removed malformed events from database.")
        return true
    }

    override fun removeAllEvents(): Boolean {
        eventStore.removeAllEvents()
        return true
    }

    override fun getSize(): Long {
        TODO("Not yet implemented")
    }

    override fun getEmittableEvents(queryLimit: Int): MutableList<EmitterEvent> {
        TODO("Not yet implemented")
    }

}