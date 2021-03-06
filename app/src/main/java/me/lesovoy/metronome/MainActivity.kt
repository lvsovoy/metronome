package me.lesovoy.metronome

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioManager
import android.media.SoundPool
import android.media.ToneGenerator
import android.os.Bundle
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*


class Preset(var pbpm: Int, var pbeatpattern: MutableList<Int>) {
    override fun toString(): String {
        return "[" + this.pbpm + " " + this.pbeatpattern + "]"
    }
}

class MainActivity : AppCompatActivity() {


    var beatpattern = mutableListOf(0, 1, 1, 1)
    var totalSteps = beatpattern.size - 1
    var currentStep = 0
    var isPlaying = false
    var PresetList: MutableList<Preset> = mutableListOf(Preset(66, mutableListOf(0, 1, 1, 0)), Preset(122, mutableListOf(1, 0, 1, 0)))
    val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.app_toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val pref = applicationContext.getSharedPreferences("appPref", 0)
        val editor = pref.edit()

        val bpm = findViewById<EditText>(R.id.bpm)
        bpm.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (bpm.editableText.toString() >= 0.toString() && bpm.editableText.toString() <= 300.toString()) {
                    editor.putInt("TapBpm", bpm.editableText.toString().toInt()).apply()
                }
            }
        })

        val vibrator: Vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        var weakVibration: VibrationEffect = VibrationEffect.createOneShot(pref.getInt("weak_vibration", 1).toLong(), VibrationEffect.DEFAULT_AMPLITUDE)
        var strongVibration: VibrationEffect = VibrationEffect.createOneShot(pref.getInt("strong_vibration", 50).toLong(), VibrationEffect.DEFAULT_AMPLITUDE)

        val sp = SoundPool.Builder().setMaxStreams(2).build()
        var tick = sp.load(this, R.raw.tick_w, 1)
        var tock = sp.load(this, R.raw.tock_w, 1)

        val tg = ToneGenerator(AudioManager.STREAM_MUSIC, 100)

        var g = 0
        var b = 0

        currentStep = -1


        val runnable = object : Runnable {
            override fun run() {
                var i = 120
                var maxlength = 500L

                totalSteps = beatpattern.size - 1
                if (currentStep >= 0) {
                    if (bpm.editableText.toString() != "") {
                        i = bpm.editableText.toString().toInt()
                        if (bpm.editableText.toString().toInt() < 301) {
                            if (i != 0 && i < 301 && i > 0) {
                                maxlength = 60000L / i.toLong()
                                if (i != 0 && i < 301 && i > 0) {
                                    when (beatpattern.elementAt(currentStep).toInt()) {
                                        0 -> {
                                            if (pref.getString("soundPref", "Digital").equals("Realistic")) {
                                                sp.play(tick, 1f, 1f, 0, 0, 1f)
                                            }
                                            if (pref.getString("soundPref", "Digital").equals("Digital")) {
                                                tg.startTone(3, 10)
                                            }
                                            if (pref.getBoolean("global_vibration", false)) {
                                                vibrator.vibrate(strongVibration)
                                            }

                                            if (currentStep >= totalSteps) {
                                                currentStep = 0
                                            } else {
                                                currentStep++
                                            }
                                            if (pref.getBoolean("global_visual", false)) {
                                                background.setBackgroundColor(Color.rgb(255, 0, b))
                                                if (b + 100 < 255) {
                                                    b += 100
                                                } else {
                                                    b = 0
                                                }
                                            }
                                        }
                                        1 -> {
                                            if (pref.getString("soundPref", "Digital").equals("Realistic")) {
                                                sp.play(tock, 1f, 1f, 0, 0, 1f)
                                            }
                                            if (pref.getString("soundPref", "Digital").equals("Digital")) {
                                                tg.startTone(1, 10)
                                            }
                                            if (pref.getBoolean("global_vibration", false)) {
                                                vibrator.vibrate(weakVibration)
                                            }
                                            if (currentStep >= totalSteps) {
                                                currentStep = 0
                                            } else {
                                                currentStep++
                                            }
                                            if (pref.getBoolean("global_visual", false)) {
                                                background.setBackgroundColor(Color.rgb(0, g, 255))
                                                if (g + 100 < 255) {
                                                    g += 100
                                                } else {
                                                    g = 0
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            bpm.setText(300.toString())
                        }
                    } else {
                        bpm.setText(1.toString())
                    }
                } else {
                    sp.play(tick, 0f, 0f, 0, 0, 1f)
                    currentStep++
                }

                handler.postDelayed(this, maxlength)
            }
        }


        //playpause button
        val playpause = findViewById<ToggleButton>(R.id.playpause)

        playpause.setOnCheckedChangeListener { _, isChecked ->
            run {
                if (isChecked) {
                    isPlaying = true

                    currentStep = 0
                    handler.post(runnable)

                } else {
                    isPlaying = false
                    handler.removeCallbacks(runnable)
                    b = 0
                    g = 0
                    background.setBackgroundColor(0xfafafa)
                }
            }
        }

        //plus/minus buttons
        val plus = findViewById<Button>(R.id.plus)
        plus.setOnLongClickListener {
            if (bpm.editableText.toString().toInt() in 0..290) {
                Toast.makeText(this, "+10", Toast.LENGTH_SHORT).show()
                var i = bpm.editableText.toString().toInt() + 10
                bpm.setText(i.toString())
                true

            } else {
                Toast.makeText(this, "cant be > 300", Toast.LENGTH_SHORT).show()
                bpm.setText(300.toString())
                true
            }
        }

        plus.setOnClickListener {
            if (bpm.editableText.toString() >= 0.toString() && bpm.editableText.toString().toInt() < 300) {
                var i = bpm.editableText.toString().toInt() + 1
                bpm.setText(i.toString())
            } else {
                bpm.setText(1.toString())
            }
        }


        val minus = findViewById<Button>(R.id.minus)
        var i = bpm.editableText.toString()

        minus.setOnLongClickListener {
            if (bpm.editableText.toString() != "" && bpm.editableText.toString().toInt() >= 10) {
                Toast.makeText(this, "-10", Toast.LENGTH_SHORT).show()
                val i = bpm.editableText.toString().toInt() - 10
                bpm.setText(i.toString())
                true
            } else {
                Toast.makeText(this, "cant be < 0", Toast.LENGTH_SHORT).show()
                true
            }
        }

        minus.setOnClickListener {
            if (bpm.editableText.toString() != "" && bpm.editableText.toString().toInt() > 0) {
                var i = bpm.editableText.toString().toInt() - 1
                bpm.setText(i.toString())
            } else {
                Toast.makeText(this, "cant be < 0", Toast.LENGTH_SHORT).show()
            }


        }

        val beatpatternLayoutManager = LinearLayoutManager(this)
        beatpatternLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        BeatpatternLayout.layoutManager = beatpatternLayoutManager

        val beatpatternAdapter = BeatpatternAdapter(this, beatpattern)
        BeatpatternLayout.adapter = beatpatternAdapter

        val beatAdd = findViewById<ImageView>(R.id.beat_add)
        beatAdd.setOnClickListener {
            beatpattern.add(0)
            beatpatternAdapter.notifyItemChanged(beatpattern.size)
        }

        val presetLayoutManager = GridLayoutManager(this, 2)
        presetLayoutManager.orientation = GridLayoutManager.VERTICAL
        PresetLayout.layoutManager = presetLayoutManager

        val presetAdapter = PresetAdapter(this, PresetList)
        PresetLayout.adapter = presetAdapter

        val fab = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        fab.setOnClickListener {
            if (PresetList.size < 8) {
                var cbeatpattern = beatpattern.toMutableList()
                var current = Preset(bpm.editableText.toString().toInt(), cbeatpattern)
                addPreset(PresetList, current)
                presetAdapter.notifyItemInserted(PresetList.size)
                Log.d("PRESET", PresetList.toString())
            }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tapTempo -> {
                val tapIntent = Intent(this@MainActivity, TapTempo::class.java)
                startActivity(tapIntent)
            }
            R.id.settings -> {
//            Launch settings activity
                val settingsIntent = Intent(this, SettingsActivity::class.java)
                startActivity(settingsIntent)
            }
            R.id.about -> {
//            Launch about activity
                val aboutIntent = Intent(this, AboutActivity::class.java)
                startActivity(aboutIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun addPreset(PresetList: MutableList<Preset>, Preset: Preset) {
        PresetList.add(Preset)
    }

    fun setPreset(preset: Preset) {
        bpm.setText(preset.pbpm.toString())
    }

    override fun onResume() {
        val pref = applicationContext.getSharedPreferences("appPref", 0)
        val editor = pref.edit()
        val bpm = findViewById<EditText>(R.id.bpm)
        bpm.setText(pref.getInt("TapBpm", 120).toString())
        super.onResume()
    }

}
