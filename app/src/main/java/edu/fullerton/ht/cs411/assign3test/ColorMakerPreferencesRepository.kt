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
    suspend fun saveValue(color: String, value: Float) {
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
    fun getValue(color: String): Flow<Float> {
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
            if (color == "red") {
                putFloat("RED_VALUE", value)
            } else if (color == "green") {
                putFloat("GREEN_VALUE", value)
            } else if (color == "blue") {
                putFloat("BLUE_VALUE", value)
            }
            apply()
        }
    }

    // gets color values from shared preferences
    fun getColorValues(color: String): Float {
        if (color == "red") {
            return sharedPrefs.getFloat("RED_VALUE", 0f)
        } else if (color == "green") {
            return sharedPrefs.getFloat("GREEN_VALUE", 0f)
        } else if (color == "blue") {
            return sharedPrefs.getFloat("BLUE_VALUE", 0f)
        } else {
            return 0f
        }
    }

    // saves switch states for each color
    fun saveSwitch(color: String, isChecked: Boolean) {
        with(sharedPrefs.edit()) {
            if (color == "red") {
                putBoolean("RED_SWITCH", isChecked)
            } else if (color == "green") {
                putBoolean("GREEN_SWITCH", isChecked)
            } else if (color == "blue") {
                putBoolean("BLUE_SWITCH", isChecked)
            }
            apply()
        }
    }

    // gets switch states
    fun getSwitch(color: String): Boolean {
        if (color == "red") {
            return sharedPrefs.getBoolean("RED_SWITCH", false)
        } else if (color == "green") {
            return sharedPrefs.getBoolean("GREEN_SWITCH", false)
        } else if (color == "blue") {
            return sharedPrefs.getBoolean("BLUE_SWITCH", false)
        } else {
            return false
        }
    }

    // saves progress values
    fun saveProgress(color: String, progress: Int) {
        with(sharedPrefs.edit()) {
            if (color == "red") {
                putInt("RED_PROGRESS", progress)
            } else if (color == "green") {
                putInt("GREEN_PROGRESS", progress)
            } else if (color == "blue") {
                putInt("BLUE_PROGRESS", progress)
            }
            apply()
        }
    }

    // gets progress values
    fun getProgress(color: String): Int {
        if (color == "red") {
            return sharedPrefs.getInt("RED_PROGRESS", 0)
        } else if (color == "green") {
            return sharedPrefs.getInt("GREEN_PROGRESS", 0)
        } else if (color == "blue") {
            return sharedPrefs.getInt("BLUE_PROGRESS", 0)
        } else {
            return 0
        }
    }

    // saves text values
    fun saveText(color: String, value: String) {
        with(sharedPrefs.edit()) {
            if (color == "red") {
                putString("RED_TEXT", value)
            } else if (color == "green") {
                putString("GREEN_TEXT", value)
            } else if (color == "blue") {
                putString("BLUE_TEXT", value)
            }
            apply()
        }
    }

    // gets text values
    fun getText(color: String): String {
        if (color == "red") {
            return sharedPrefs.getString("RED_TEXT", "0.000") ?: "0.000"
        } else if (color == "green") {
            return sharedPrefs.getString("GREEN_TEXT", "0.000") ?: "0.000"
        } else if (color == "blue") {
            return sharedPrefs.getString("BLUE_TEXT", "0.000") ?: "0.000"
        } else {
            return "0.000"
        }
    }
}
