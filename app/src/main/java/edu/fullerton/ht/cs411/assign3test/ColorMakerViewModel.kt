package edu.fullerton.ht.cs411.assign3test

import android.app.Application
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
        loadColorValues()
    }

    // sets the color value
    fun setColorValue(color: String, value: Float) {
        if (color == "red") {
            _red.value = value
        } else if (color == "green") {
            _green.value = value
        } else if (color == "blue") {
            _blue.value = value
        }
        saveColorValues()
    }

    // loads the color values from the repository
    private fun loadColorValues() {
        viewModelScope.launch {
            // collect the value from the Flow
            _red.value = colorMakerPreferencesRepository.getColorValue("red").first()
            _green.value = colorMakerPreferencesRepository.getColorValue("green").first()
            _blue.value = colorMakerPreferencesRepository.getColorValue("blue").first()
        }
    }

    // saves the color values to the repository
    private fun saveColorValues() {
        viewModelScope.launch {
            // save the current values in DataStore
            colorMakerPreferencesRepository.saveColorValue("red", _red.value ?: 0f)
            colorMakerPreferencesRepository.saveColorValue("green", _green.value ?: 0f)
            colorMakerPreferencesRepository.saveColorValue("blue", _blue.value ?: 0f)
        }
    }

    // resets all colors back to zero
    fun resetColorValues() {
        _red.value = 0f
        _green.value = 0f
        _blue.value = 0f
        saveColorValues()
    }
}