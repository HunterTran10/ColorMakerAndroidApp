package edu.fullerton.ht.cs411.assign3test

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

// class handles the red, green, and blue color values for the app
class ColorMakerViewModel(application: Application) : AndroidViewModel(application) {

    // hold the color values inside the viewmodel
    private val _red = MutableLiveData<Float>()
    val red: LiveData<Float> get() = _red

    private val _green = MutableLiveData<Float>()
    val green: LiveData<Float> get() = _green

    private val _blue = MutableLiveData<Float>()
    val blue: LiveData<Float> get() = _blue

    private val colorMakerPreferencesRepository =
        ColorMakerPreferencesRepository(application.applicationContext)

    // runs when the viewmodel starts
    init {
        Log.i(LOG_TAG, "view model initialized")
        loadValue()
    }

    // sets the color value
    fun setColorValue(color: String, value: Float) {
        Log.i(LOG_TAG, "setting $color to $value")
        if (color == "red") {
            _red.value = value
            Log.i(LOG_TAG, "red color set to $value")
        } else if (color == "green") {
            _green.value = value
            Log.i(LOG_TAG, "green color set to $value")
        } else if (color == "blue") {
            _blue.value = value
            Log.i(LOG_TAG, "blue color set to $value")
        }
        saveValue()
    }

    // loads the color values from the repository
    private fun loadValue() {
        viewModelScope.launch {
            Log.i(LOG_TAG, "loading color values from repository")
            // collect the value from the Flow
            _red.value = colorMakerPreferencesRepository.getValue("red").first()
            _green.value = colorMakerPreferencesRepository.getValue("green").first()
            _blue.value = colorMakerPreferencesRepository.getValue("blue").first()
            Log.i(LOG_TAG, "loaded values are red: ${_red.value}, green: ${_green.value}, blue: ${_blue.value}")
        }
    }

    // saves the color values to the repository
    private fun saveValue() {
        viewModelScope.launch {
            Log.i(LOG_TAG, "saving color values to repository")
            colorMakerPreferencesRepository.saveValue("red", _red.value ?: 0f)
            colorMakerPreferencesRepository.saveValue("green", _green.value ?: 0f)
            colorMakerPreferencesRepository.saveValue("blue", _blue.value ?: 0f)
            Log.i(LOG_TAG, "saved values are red: ${_red.value}, green: ${_green.value}, blue: ${_blue.value}")
        }
    }

    // resets all colors back to zero
    fun resetValues() {
        Log.i(LOG_TAG, "resetting all colors to 0")
        _red.value = 0f
        _green.value = 0f
        _blue.value = 0f
        saveValue()
    }
}