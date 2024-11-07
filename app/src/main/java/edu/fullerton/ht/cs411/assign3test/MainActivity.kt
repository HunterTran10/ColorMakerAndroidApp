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
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

const val LOG_TAG = "Assignment3"

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            redSwitch.isChecked = false
            greenSwitch.isChecked = false
            blueSwitch.isChecked = false
            redSeekBar.progress = 0
            greenSeekBar.progress = 0
            blueSeekBar.progress = 0
            redEditText.setText("0.000")
            greenEditText.setText("0.000")
            blueEditText.setText("0.000")
            Log.i(LOG_TAG, "reset button clicked and all values reset to 0.000")
            colorBox.setBackgroundColor(Color.rgb(0, 0, 0)) // color box will be black
        }
    }

    // set the listeners for each switch, seekbar and editText
    private fun setListeners(switch: Switch, seekBar: SeekBar, editText: EditText, color: String) {

        // listener for changes in switch
        switch.setOnCheckedChangeListener { _, isChecked ->

            Log.i(LOG_TAG, "$color switch is now ${isChecked}")

            // if the switch is off, save the value and update the color box
            if (!switch.isChecked) {
                // disable seekbar and editText
                seekBar.isEnabled = false
                editText.isEnabled = false

                // save and reset color values
                if (color == "red") {
                    redSavedValue = redValue
                    redValue = 0f
                } else if (color == "green") {
                    greenSavedValue = greenValue
                    greenValue = 0f
                } else if (color == "blue") {
                    blueSavedValue = blueValue
                    blueValue = 0f
                }
                updateColorBox(color, 0f) // update the colors to 0
            } else {
                // enable seekbar and editText
                seekBar.isEnabled = true
                editText.isEnabled = true

                // restore color values and update the color box
                if (color == "red") {
                    redValue = redSavedValue
                    updateColorBox(color, redValue)
                } else if (color == "green") {
                    greenValue = greenSavedValue
                    updateColorBox(color, greenValue)
                } else if (color == "blue") {
                    blueValue = blueSavedValue
                    updateColorBox(color, blueValue)
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
                    updateColorBox(color, value)
                    Log.i(LOG_TAG, "$color seek bar value is $progress")
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
                        val rangeValue = floatValue.coerceIn(0f, 1f)// make the float value between 0.0 and 1.0
                        editText.setText(String.format("%.3f", rangeValue)) // update the edittext with the new value
                        val seekBarProgress = (rangeValue * 255).roundToInt()
                        seekBar.progress = seekBarProgress

                        Log.i(LOG_TAG, "$rangeValue was a valid input and the seek bar value is $seekBarProgress")

                        updateColorBox(color, seekBarProgress / 255f)
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
        if (color == "red") {
            redValue = value
        } else if (color == "green") {
            greenValue = value
        } else if (color == "blue") {
            blueValue = value
        }

        // convert floats to integers
        val red = (redValue * 255).roundToInt()
        val green = (greenValue * 255).roundToInt()
        val blue = (blueValue * 255).roundToInt()
        colorBox.setBackgroundColor(Color.rgb(red, green, blue)) // change color box
    }

}

