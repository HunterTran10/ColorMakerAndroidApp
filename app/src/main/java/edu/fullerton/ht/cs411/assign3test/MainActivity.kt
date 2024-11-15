package edu.fullerton.ht.cs411.assign3test

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Switch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

const val LOG_TAG = "Assignment4"

class MainActivity : AppCompatActivity() {

    private var redValue = 0f
    private var greenValue = 0f
    private var blueValue = 0f

    private lateinit var colorBox: View
    private lateinit var redSwitch: Switch
    private lateinit var greenSwitch: Switch
    private lateinit var blueSwitch: Switch
    private lateinit var redSeekBar: SeekBar
    private lateinit var greenSeekBar: SeekBar
    private lateinit var blueSeekBar: SeekBar
    private lateinit var redEditText: EditText
    private lateinit var greenEditText: EditText
    private lateinit var blueEditText: EditText
    private lateinit var resetButton: Button

    private var redSavedValue = 0f
    private var greenSavedValue = 0f
    private var blueSavedValue = 0f

    private val colorMakerViewModel: ColorMakerViewModel by viewModels()
    private val colorMakerPreferencesRepository: ColorMakerPreferencesRepository by lazy {
        ColorMakerPreferencesRepository(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i(LOG_TAG, "onCreate called")

        setContentView(R.layout.activity_main)
        colorBox = findViewById(R.id.colorBox)
        redSwitch = findViewById(R.id.switch1)
        greenSwitch = findViewById(R.id.switch2)
        blueSwitch = findViewById(R.id.switch3)
        redSeekBar = findViewById(R.id.seekBar1)
        greenSeekBar = findViewById(R.id.seekBar2)
        blueSeekBar = findViewById(R.id.seekBar3)
        redEditText = findViewById(R.id.redTextEdit)
        greenEditText = findViewById(R.id.greenTextEdit)
        blueEditText = findViewById(R.id.blueTextEdit)
        resetButton = findViewById(R.id.button)

        // disable seekbars and EditTexts when the app is opened
        redSeekBar.isEnabled = false
        greenSeekBar.isEnabled = false
        blueSeekBar.isEnabled = false
        redEditText.isEnabled = false
        greenEditText.isEnabled = false
        blueEditText.isEnabled = false

        // set initial values for SeekBars and EditTexts to 0
        redSeekBar.progress = 0
        greenSeekBar.progress = 0
        blueSeekBar.progress = 0
        redEditText.setText("0.000")
        greenEditText.setText("0.000")
        blueEditText.setText("0.000")

        // color box will be initially black
        colorBox.setBackgroundColor(Color.rgb(0, 0, 0))

        // set min and max values for SeekBars
        redSeekBar.min = 0
        greenSeekBar.min = 0
        blueSeekBar.min = 0
        redSeekBar.max = 255
        greenSeekBar.max = 255
        blueSeekBar.max = 255

        // view model observers to update when values are changed
        colorMakerViewModel.red.observe(this) { value ->
            Log.i(LOG_TAG, "Updating red value to $value")
            redSeekBar.progress = (value * 255).roundToInt()
            redEditText.setText(String.format("%.3f", value))
            updateColorBox("red", value)
        }
        colorMakerViewModel.green.observe(this) { value ->
            Log.i(LOG_TAG, "Updating green value to $value")
            greenSeekBar.progress = (value * 255).roundToInt()
            greenEditText.setText(String.format("%.3f", value))
            updateColorBox("green", value)
        }
        colorMakerViewModel.blue.observe(this) { value ->
            Log.i(LOG_TAG, "Updating blue value to $value")
            blueSeekBar.progress = (value * 255).roundToInt()
            blueEditText.setText(String.format("%.3f", value))
            updateColorBox("blue", value)
        }

        // set the listeners for switches, seekbars and editTexts
        setListeners(redSwitch, redSeekBar, redEditText, "red")
        setListeners(greenSwitch, greenSeekBar, greenEditText, "green")
        setListeners(blueSwitch, blueSeekBar, blueEditText, "blue")

        // listener for reset button
        resetButton.setOnClickListener {
            // reset everything and change the colorbox
            redValue = 0f
            greenValue = 0f
            blueValue = 0f
            redSavedValue = 0f
            greenSavedValue = 0f
            blueSavedValue = 0f

            // reset values in colorMakerPreferencesRepository
            colorMakerPreferencesRepository.saveColorValues("red", redValue)
            colorMakerPreferencesRepository.saveColorValues("green", greenValue)
            colorMakerPreferencesRepository.saveColorValues("blue", blueValue)

            redSwitch.isChecked = false
            greenSwitch.isChecked = false
            blueSwitch.isChecked = false

            // update color values in colorMakerViewModel
            colorMakerViewModel.setColorValue("red", 0f)
            colorMakerViewModel.setColorValue("green", 0f)
            colorMakerViewModel.setColorValue("blue", 0f)

            // change the colorbox
            updateColorBox("red", 0f)
            updateColorBox("green", 0f)
            updateColorBox("blue", 0f)

            // reset color values in colorMakerViewModel
            colorMakerViewModel.resetValues()

            colorBox.setBackgroundColor(Color.rgb(0, 0, 0)) // color box will be black
            Log.i(LOG_TAG, "reset button clicked and all values reset to 0.000")
        }

        // restore saved values from colorMakerPreferencesRepository
        redSwitch.isChecked = colorMakerPreferencesRepository.getSwitch("red")
        greenSwitch.isChecked = colorMakerPreferencesRepository.getSwitch("green")
        blueSwitch.isChecked = colorMakerPreferencesRepository.getSwitch("blue")
        redSeekBar.progress = colorMakerPreferencesRepository.getProgress("red")
        greenSeekBar.progress = colorMakerPreferencesRepository.getProgress("green")
        blueSeekBar.progress = colorMakerPreferencesRepository.getProgress("blue")
        redEditText.setText(colorMakerPreferencesRepository.getText("red"))
        greenEditText.setText(colorMakerPreferencesRepository.getText("green"))
        blueEditText.setText(colorMakerPreferencesRepository.getText("blue"))

        // restore instance state
        if (savedInstanceState != null) {
            Log.i(LOG_TAG, "restoring saved instance state")

            // restore seekbar progress values
            redSeekBar.progress = savedInstanceState.getInt("RED_PROGRESS", 0)
            greenSeekBar.progress = savedInstanceState.getInt("GREEN_PROGRESS", 0)
            blueSeekBar.progress = savedInstanceState.getInt("BLUE_PROGRESS", 0)

            // restore switch state
            redSwitch.isChecked = savedInstanceState.getBoolean("RED_SWITCH", false)
            greenSwitch.isChecked = savedInstanceState.getBoolean("GREEN_SWITCH", false)
            blueSwitch.isChecked = savedInstanceState.getBoolean("BLUE_SWITCH", false)

            // restore text input values
            redEditText.setText(savedInstanceState.getString("RED_TEXT", "0.000"))
            greenEditText.setText(savedInstanceState.getString("GREEN_TEXT", "0.000"))
            blueEditText.setText(savedInstanceState.getString("BLUE_TEXT", "0.000"))

            // restore saved color values
            redSavedValue = savedInstanceState.getFloat("RED_SAVED_VALUE", 0f)
            greenSavedValue = savedInstanceState.getFloat("GREEN_SAVED_VALUE", 0f)
            blueSavedValue = savedInstanceState.getFloat("BLUE_SAVED_VALUE", 0f)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(LOG_TAG, "saving instance state")

        // save seekbar progress values
        outState.putInt("RED_PROGRESS", redSeekBar.progress)
        outState.putInt("GREEN_PROGRESS", greenSeekBar.progress)
        outState.putInt("BLUE_PROGRESS", blueSeekBar.progress)

        // save switch state
        outState.putBoolean("RED_SWITCH", redSwitch.isChecked)
        outState.putBoolean("GREEN_SWITCH", greenSwitch.isChecked)
        outState.putBoolean("BLUE_SWITCH", blueSwitch.isChecked)

        // save text input values
        outState.putString("RED_TEXT", redEditText.text.toString())
        outState.putString("GREEN_TEXT", greenEditText.text.toString())
        outState.putString("BLUE_TEXT", blueEditText.text.toString())

        // save the color values when switches are off
        outState.putFloat("RED_SAVED_VALUE", redSavedValue)
        outState.putFloat("GREEN_SAVED_VALUE", greenSavedValue)
        outState.putFloat("BLUE_SAVED_VALUE", blueSavedValue)

    }

    // set the listeners for each switch, seekbar and editText
    private fun setListeners(switch: Switch, seekBar: SeekBar, editText: EditText, color: String) {

        // listener for changes in switch
        switch.setOnCheckedChangeListener { _, isChecked ->

            Log.i(LOG_TAG, "$color switch is now ${isChecked}")

            // save switch state to colorMakerPreferencesRepository
            colorMakerPreferencesRepository.saveSwitch(color, isChecked)

            // save the current value
            if (!isChecked) {
                if (color == "red") {
                    colorMakerPreferencesRepository.saveColorValues("red", redValue)
                } else if (color == "green") {
                    colorMakerPreferencesRepository.saveColorValues("green", greenValue)
                } else if (color == "blue") {
                    colorMakerPreferencesRepository.saveColorValues("blue", blueValue)
                }
            }

            // save seekbar progress
            colorMakerPreferencesRepository.saveProgress(color, seekBar.progress)

            // save editText value
            colorMakerPreferencesRepository.saveText(color, editText.text.toString())

            // if the switch is off, save the value and update the color box
            if (!isChecked) {
                // disable seekbar and editText
                seekBar.isEnabled = false
                editText.isEnabled = false

                // save and reset color values
                if (color == "red") {
                    redSavedValue = redValue
                    redValue = 0f
                    colorMakerViewModel.setColorValue("red", redValue) // update color value in the view model
                } else if (color == "green") {
                    greenSavedValue = greenValue
                    greenValue = 0f
                    colorMakerViewModel.setColorValue("green", greenValue)
                } else if (color == "blue") {
                    blueSavedValue = blueValue
                    blueValue = 0f
                    colorMakerViewModel.setColorValue("blue", blueValue)
                }
            } else {
                // enable seekbar and editText
                seekBar.isEnabled = true
                editText.isEnabled = true

                // restore color values
                if (color == "red") {
                    // get the value from colorMakerPreferencesRepository
                    redValue = colorMakerPreferencesRepository.getColorValues("red")
                    // update the color value in colorMakerViewModel
                    colorMakerViewModel.setColorValue("red", redValue)
                }
                else if (color == "green") {
                    greenValue = colorMakerPreferencesRepository.getColorValues("green")
                    colorMakerViewModel.setColorValue("green", greenValue)
                }
                else if (color == "blue") {
                    blueValue = colorMakerPreferencesRepository.getColorValues("blue")
                    colorMakerViewModel.setColorValue("blue", blueValue)
                }

                Log.i(LOG_TAG, "$color switch turned on and restoring value")
            }
        }

        // listener for seek bar to update color values
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (switch.isChecked) {
                    val value = progress / 255f // convert progress to float
                    editText.setText(String.format("%.3f", value)) // update edit text
                    colorMakerViewModel.setColorValue(color, value)
                    updateColorBox(color, value)
                    Log.i(LOG_TAG, "$color seek bar value is $progress")

                    // save the progress values in colorMakerPreferencesRepository
                    colorMakerPreferencesRepository.saveProgress("red", seekBar.progress)
                    colorMakerPreferencesRepository.saveProgress("green", seekBar.progress)
                    colorMakerPreferencesRepository.saveProgress("blue ", seekBar.progress)

                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        editText.setOnEditorActionListener { _, actionId, event ->
            // check if the user pressed a key or the user hit enter in the editText
            if (event?.action == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_DONE) {
                val userInput = editText.text.toString() // assign the value from the EditText
                if (userInput.isNotEmpty()) {
                    val floatValue = userInput.toFloatOrNull() // try to convert the input to a float number
                    if (floatValue != null) {
                        val rangeValue = floatValue.coerceIn(0f, 1f) // make the float value between 0.0 and 1.0
                        editText.setText(String.format("%.3f", rangeValue)) // update the edittext with the new value
                        val seekBarProgress = (rangeValue * 255).roundToInt()
                        seekBar.progress = (rangeValue * 255).roundToInt()
                        colorMakerViewModel.setColorValue(color, seekBarProgress / 255f)
                        updateColorBox(color, seekBarProgress / 255f)

                        Log.i(LOG_TAG, "$rangeValue was a valid input and the seek bar value is $seekBarProgress")

                        // save the text values in colorMakerPreferencesRepository
                        colorMakerPreferencesRepository.saveText("red", editText.text.toString())
                        colorMakerPreferencesRepository.saveText("green", editText.text.toString())
                        colorMakerPreferencesRepository.saveText("blue ", editText.text.toString())
                    }
                }
                true // the action was handled
            } else {
                false //  the system will process unhandled actions
            }
        }
    }

    // update the changes and the color box
    private fun updateColorBox(color: String, value: Float) {
        if (color == "red" && redValue != value) {
            redValue = value
            colorMakerViewModel.setColorValue("red", redValue)
        } else if (color == "green" && greenValue != value) {
            greenValue = value
            colorMakerViewModel.setColorValue("green", greenValue)
        } else if (color == "blue" && blueValue != value) {
            blueValue = value
            colorMakerViewModel.setColorValue("blue", blueValue)
        }

        // get the value in colorMakerViewModel and convert the float to integer
        val red = ((colorMakerViewModel.red.value ?: 0f) * 255).roundToInt()
        val green = ((colorMakerViewModel.green.value ?: 0f) * 255).roundToInt()
        val blue = ((colorMakerViewModel.blue.value ?: 0f) * 255).roundToInt()
        colorBox.setBackgroundColor(Color.rgb(red, green, blue)) // change color box
    }
}