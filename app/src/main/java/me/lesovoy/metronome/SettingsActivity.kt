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


        val colorArray = intArrayOf(0xFFEF5350.toInt(), 0xFFec407a.toInt(), 0xffab47bc.toInt(), 0xff7e57c2.toInt(), 0xff5c6bc0.toInt(), 0xff42a5f5.toInt(), 0xff29b6f6.toInt(), 0xff26c6da.toInt(), 0xff26a69a.toInt(), 0xff66bb6a.toInt(), 0xff9ccc65.toInt(), 0xffd4e157.toInt(), 0xffffee58.toInt(), 0xffffca28.toInt(), 0xffffa726.toInt(), 0xffff7043.toInt(), 0xff8d6e63.toInt(), 0xffbdbdbd.toInt(), 0xff78909c.toInt())

        val colorPicker = findViewById<View>(R.id.mainPicker) as LineColorPicker

        colorPicker.colors = colorArray

        colorPicker.setSelectedColor(pref.getInt("main_colour", Color.RED))

        colorPicker.setOnColorChangedListener(OnColorChangedListener {
            c -> Log.d("COLOUR PICKER", "Selected color " + Integer.toHexString(c))
            editor.putInt("main_colour",colorPicker.color).apply()
        })




        val colorPicker1 = findViewById<View>(R.id.offPicker) as LineColorPicker

        colorPicker1.colors = colorArray

        colorPicker1.setSelectedColor(pref.getInt("off_colour",Color.BLUE))

        colorPicker1.setOnColorChangedListener(OnColorChangedListener { c -> Log.d("COLOUR PICKER", "Selected color " + Integer.toHexString(c))
            editor.putInt("off_colour",colorPicker1.color).apply()
        })
    }


}

