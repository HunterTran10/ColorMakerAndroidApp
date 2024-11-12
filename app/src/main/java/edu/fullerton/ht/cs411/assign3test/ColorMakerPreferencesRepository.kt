package edu.fullerton.ht.cs411.assign3test

import android.content.Context
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// sets up data store for saving color
val Context.dataStore by preferencesDataStore(name = "color_prefs")

class ColorMakerPreferencesRepository(context: Context) {

    private val dataStore = context.dataStore

    private val redKey = floatPreferencesKey("red")
    private val greenKey = floatPreferencesKey("green")
    private val blueKey = floatPreferencesKey("blue")

    // saves a color value to data store
    suspend fun saveColorValue(color: String, value: Float) {
        dataStore.edit { prefs ->
            if (color == "red") {
                prefs[redKey] = value
            } else if (color == "green") {
                prefs[greenKey] = value
            } else if (color == "blue") {
                prefs[blueKey] = value
            }
        }
    }

    // gets a color value from data store
    fun getColorValue(color: String): Flow<Float> {
        return dataStore.data.map { prefs ->
            if (color == "red") {
                prefs[redKey] ?: 0f
            } else if (color == "green") {
                prefs[greenKey] ?: 0f
            } else if (color == "blue") {
                prefs[blueKey] ?: 0f
            } else {
                0f
            }
        }
    }


    private val sharedPrefs = context.getSharedPreferences("ColorMakerAppPreferences", Context.MODE_PRIVATE)

    // saves color values to shared preferences
    fun saveColorValues(color: String, value: Float) {
        with(sharedPrefs.edit()) {
            when (color) {
                "red" -> putFloat("RED_VALUE", value)
                "green" -> putFloat("GREEN_VALUE", value)
                "blue" -> putFloat("BLUE_VALUE", value)
            }
            apply()
        }
    }

    // gets color values from shared preferences
    fun getColorValues(color: String): Float {
        return when (color) {
            "red" -> sharedPrefs.getFloat("RED_VALUE", 0f)
            "green" -> sharedPrefs.getFloat("GREEN_VALUE", 0f)
            "blue" -> sharedPrefs.getFloat("BLUE_VALUE", 0f)
            else -> 0f
        }
    }

    // saves switch states for each color
    fun saveSwitchState(color: String, isChecked: Boolean) {
        with(sharedPrefs.edit()) {
            when (color) {
                "red" -> putBoolean("RED_SWITCH", isChecked)
                "green" -> putBoolean("GREEN_SWITCH", isChecked)
                "blue" -> putBoolean("BLUE_SWITCH", isChecked)
            }
            apply()
        }
    }

    // gets switch states
    fun getSwitchState(color: String): Boolean {
        return when (color) {
            "red" -> sharedPrefs.getBoolean("RED_SWITCH", false)
            "green" -> sharedPrefs.getBoolean("GREEN_SWITCH", false)
            "blue" -> sharedPrefs.getBoolean("BLUE_SWITCH", false)
            else -> false
        }
    }

    // saves progress values
    fun saveProgress(color: String, progress: Int) {
        with(sharedPrefs.edit()) {
            when (color) {
                "red" -> putInt("RED_PROGRESS", progress)
                "green" -> putInt("GREEN_PROGRESS", progress)
                "blue" -> putInt("BLUE_PROGRESS", progress)
            }
            apply()
        }
    }

    // gets progress values
    fun getProgress(color: String): Int {
        return when (color) {
            "red" -> sharedPrefs.getInt("RED_PROGRESS", 0)
            "green" -> sharedPrefs.getInt("GREEN_PROGRESS", 0)
            "blue" -> sharedPrefs.getInt("BLUE_PROGRESS", 0)
            else -> 0
        }
    }

    // saves text values
    fun saveTextValue(color: String, value: String) {
        with(sharedPrefs.edit()) {
            when (color) {
                "red" -> putString("RED_TEXT", value)
                "green" -> putString("GREEN_TEXT", value)
                "blue" -> putString("BLUE_TEXT", value)
            }
            apply()
        }
    }

    // gets text values
    fun getTextValue(color: String): String {
        return when (color) {
            "red" -> sharedPrefs.getString("RED_TEXT", "0.000") ?: "0.000"
            "green" -> sharedPrefs.getString("GREEN_TEXT", "0.000") ?: "0.000"
            "blue" -> sharedPrefs.getString("BLUE_TEXT", "0.000") ?: "0.000"
            else -> "0.000"
        }
    }
}