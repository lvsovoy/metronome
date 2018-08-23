package me.lesovoy.metronome

import android.app.Fragment
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.preference.PreferenceFragment
import android.preference.PreferenceScreen
import android.support.constraint.R.attr.content
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import java.text.AttributedCharacterIterator
import android.content.SharedPreferences.Editor
import android.graphics.Color
import android.util.Log
import android.widget.*
import kotlinx.android.synthetic.main.settings_activity.*
import uz.shift.colorpicker.OnColorChangedListener
import uz.shift.colorpicker.LineColorPicker




class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        setSupportActionBar(findViewById(R.id.settings_toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val pref = applicationContext.getSharedPreferences("appPref", 0)
        val editor = pref.edit()

        val vibrator: Vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        var weakVibration: VibrationEffect = VibrationEffect.createOneShot(10, VibrationEffect.DEFAULT_AMPLITUDE)

        val vibrationGlobal = findViewById<Switch>(R.id.vibration_global)
        vibrationGlobal.isChecked = pref.getBoolean("global_vibration", false)

        vibrationGlobal.setOnCheckedChangeListener { _, isChecked ->
            run {
                if (isChecked) {
                    editor.putBoolean("global_vibration", true).apply()
                    strongVibrationSB.isEnabled = true
                    weakVibrationSB.isEnabled = true
                } else {
                    editor.putBoolean("global_vibration", false).apply()
                    strongVibrationSB.isEnabled = false
                    weakVibrationSB.isEnabled = false
                }
            }
        }

        val strongVibrationSB = findViewById<SeekBar>(R.id.strongVibrationSB)
        strongVibrationSB.progress = pref.getInt("strong_vibration", 50)
        strongVibrationSB.isEnabled = pref.getBoolean("global_vibration", false)
        strongVibrationSB.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                var strongVibration: VibrationEffect = VibrationEffect.createOneShot(i.toLong(), VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator.vibrate(strongVibration)
                editor.putInt("strong_vibration", i)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                editor.apply()
            }

        })
        val weakVibrationSB = findViewById<SeekBar>(R.id.weakVibrationSB)
        weakVibrationSB.progress = pref.getInt("weak_vibration", 10)
        weakVibrationSB.isEnabled = pref.getBoolean("global_vibration", false)
        weakVibrationSB.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                var weakVibration: VibrationEffect = VibrationEffect.createOneShot(i.toLong(), VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator.vibrate(weakVibration)
                editor.putInt("weak_vibration", i)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                editor.apply()
            }

        })

        val colorArray = intArrayOf(Color.GREEN, Color.BLUE, Color.YELLOW, Color.RED, Color.CYAN, Color.MAGENTA, Color.LTGRAY, Color.GRAY)

        val colorPicker = findViewById<View>(R.id.mainPicker) as LineColorPicker

        colorPicker.colors = colorArray

        colorPicker.setSelectedColor(Color.RED)

        colorPicker.setOnColorChangedListener(OnColorChangedListener { c -> Log.d("COLOUR PICKER", "Selected color " + Integer.toHexString(c)) })

        val color = colorPicker.color


        val colorPicker1 = findViewById<View>(R.id.offPicker) as LineColorPicker

        colorPicker1.colors = colorArray

        colorPicker1.setSelectedColor(Color.RED)

        colorPicker1.setOnColorChangedListener(OnColorChangedListener { c -> Log.d("COLOUR PICKER", "Selected color " + Integer.toHexString(c)) })

        val color1 = colorPicker.color

    }


}

