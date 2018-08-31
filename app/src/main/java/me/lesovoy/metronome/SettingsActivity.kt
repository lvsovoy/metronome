package me.lesovoy.metronome

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Switch
import kotlinx.android.synthetic.main.settings_activity.*
import uz.shift.colorpicker.LineColorPicker
import uz.shift.colorpicker.OnColorChangedListener


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        setSupportActionBar(findViewById(R.id.settings_toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val vibr_info = findViewById<ImageView>(R.id.vibr_info)
        vibr_info.setOnClickListener {
            val vInfo = AlertDialog.Builder(this@SettingsActivity)
            vInfo.setTitle("Warning")
            vInfo.setIcon(R.drawable.ic_outline_warning_24px)
            vInfo.setMessage("Prolonged use may negatively impact battery life")
            val dialog: AlertDialog = vInfo.create()
            dialog.show()
        }
        val visual_info = findViewById<ImageView>(R.id.visual_info)
        visual_info.setOnClickListener {
            val visInfo = AlertDialog.Builder(this@SettingsActivity)
            visInfo.setTitle("Warning")
            visInfo.setIcon(R.drawable.ic_outline_warning_24px)
            visInfo.setMessage("High brightness can reduce both battery life and cause screen burn-in.\n\n" + "Bright flashing may cause seizures.")
            val dialog: AlertDialog = visInfo.create()
            dialog.show()
        }

        val pref = applicationContext.getSharedPreferences("appPref", 0)
        val editor = pref.edit()


        val soundPref = findViewById<ConstraintLayout>(R.id.SoundSettingLayout)
        CurrentSound.text = pref.getString("soundPref", "Digital")

        soundPref.setOnClickListener {
            lateinit var dialog: AlertDialog
            val arraySound = arrayOf("Digital", "Realistic")

            val builder = AlertDialog.Builder(this)

            builder.setTitle("Choose beat sound")
            var checkedItem = 0
            if (pref.getString("soundPref", "Digital").equals("Digital")) {
                checkedItem = 0
            } else {
                checkedItem = 1
            }
            builder.setSingleChoiceItems(arraySound, checkedItem) { _, which ->
                editor.putString("soundPref", arraySound[which]).apply()
                Log.d("SETTING DIALOG", "selected item: " + pref.getString("soundPref", "Digital"))
                CurrentSound.text = pref.getString("soundPref", "Digital")
                dialog.dismiss()
            }


            // Initialize the AlertDialog using builder object
            dialog = builder.create()

            // Finally, display the alert dialog
            dialog.show()
        }


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

        val visualGlobal = findViewById<Switch>(R.id.visual_global)
        visualGlobal.isChecked = pref.getBoolean("global_visual", false)

        visualGlobal.setOnCheckedChangeListener { _, isChecked ->
            run {
                if (isChecked) {
                    editor.putBoolean("global_visual", true).apply()
                } else {
                    editor.putBoolean("global_visual", false).apply()
                }
            }
        }


        val colorArray = intArrayOf(0xFFEF5350.toInt(), 0xFFec407a.toInt(), 0xffab47bc.toInt(), 0xff7e57c2.toInt(), 0xff5c6bc0.toInt(), 0xff42a5f5.toInt(), 0xff29b6f6.toInt(), 0xff26c6da.toInt(), 0xff26a69a.toInt(), 0xff66bb6a.toInt(), 0xff9ccc65.toInt(), 0xffd4e157.toInt(), 0xffffee58.toInt(), 0xffffca28.toInt(), 0xffffa726.toInt(), 0xffff7043.toInt(), 0xff8d6e63.toInt(), 0xffbdbdbd.toInt(), 0xff78909c.toInt())

        val colorPicker = findViewById<View>(R.id.mainPicker) as LineColorPicker

        colorPicker.colors = colorArray

        colorPicker.setSelectedColor(pref.getInt("main_colour", 0xff66bb6a.toInt()))

        colorPicker.setOnColorChangedListener(OnColorChangedListener { c ->
            Log.d("COLOUR PICKER", "Selected color " + Integer.toHexString(c))
            editor.putInt("main_colour", colorPicker.color).apply()
        })


        val colorPicker1 = findViewById<View>(R.id.offPicker) as LineColorPicker

        colorPicker1.colors = colorArray

        colorPicker1.setSelectedColor(pref.getInt("off_colour", 0xff9ccc65.toInt()))

        colorPicker1.setOnColorChangedListener(OnColorChangedListener { c ->
            Log.d("COLOUR PICKER", "Selected color " + Integer.toHexString(c))
            editor.putInt("off_colour", colorPicker1.color).apply()
        })
    }


}

