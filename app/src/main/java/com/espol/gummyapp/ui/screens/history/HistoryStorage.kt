package com.espol.gummyapp.ui.history

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object HistoryStorage {

    private const val PREFS_NAME = "gomi_history"
    private const val KEY_RECORDS = "records"

    private val gson = Gson()

    fun saveRecord(context: Context, record: HistoryRecord) {
        val records = getRecords(context).toMutableList()
        records.add(record)

        val json = gson.toJson(records)
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_RECORDS, json)
            .apply()
    }

    fun getRecords(context: Context): List<HistoryRecord> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_RECORDS, null) ?: return emptyList()

        val type = object : TypeToken<List<HistoryRecord>>() {}.type
        return gson.fromJson(json, type)
    }
}
