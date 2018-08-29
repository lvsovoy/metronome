package me.lesovoy.metronome

//import com.sun.org.apache.xerces.internal.util.DOMUtil.getParent
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioManager
import android.media.SoundPool
import android.media.ToneGenerator
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.launch


//import sun.security.krb5.Confounder.intValue


class Preset(var pbpm: Int, var pbeatpattern: MutableList<Int>) {

    override fun toString(): String {
        return "[" + this.pbpm + " " + this.pbeatpattern + "]"
    }

}

class MainActivity : AppCompatActivity() {

    //    var preset: Preset
    var beatpattern = mutableListOf(0, 1, 1, 1)
    // 0 - Tick 1 - Tock
    private var totalSteps = beatpattern.size - 1
    private var currentStep = 0
    var isPlaying = false

    var PresetList: MutableList<Preset> = mutableListOf(Preset(66, mutableListOf(0, 1, 1, 0)), Preset(122, mutableListOf(1, 0, 1, 0)))


    fun setCurrentStep(i: Int) {
        currentStep = i
    }

    fun getCurrentStep(): Int {
        return currentStep
    }

    fun setTotalSteps(i: Int) {
        totalSteps = i
    }

    fun getTotalSteps(): Int {
        return totalSteps
    }


    @SuppressLint("ClickableViewAccessibility", "WrongViewCast", "ResourceType")
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
                playpause.isChecked = false
                if (bpm.editableText.toString() >= 0.toString() && bpm.editableText.toString() <= 300.toString()) {
                    editor.putInt("TapBpm", bpm.editableText.toString().toInt()).apply()
                }
            }
        })


        //playpause button
        val playpause = findViewById<ToggleButton>(R.id.playpause)

        playpause.setOnCheckedChangeListener { _, isChecked ->
            run {
                if (isChecked) {
                    isPlaying = true

                    val vibrator: Vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    var weakVibration: VibrationEffect = VibrationEffect.createOneShot(pref.getInt("weak_vibration", 1).toLong(), VibrationEffect.DEFAULT_AMPLITUDE)
                    var strongVibration: VibrationEffect = VibrationEffect.createOneShot(pref.getInt("strong_vibration", 50).toLong(), VibrationEffect.DEFAULT_AMPLITUDE)

                    val sp = SoundPool.Builder().setMaxStreams(2).build()
                    var tick = sp.load(this, R.raw.tick_w, 1)
                    var tock = sp.load(this, R.raw.tock_w, 1)

                    val tg = ToneGenerator(AudioManager.STREAM_MUSIC, 100)

                    setCurrentStep(-1)
                    var curtime = System.currentTimeMillis()

                    launch {
                        while (isPlaying) {
                            if (currentStep >= 0) {
                                if (bpm.editableText.toString() != "") {
                                    if (bpm.editableText.toString().toInt() < 301) {
                                        var i = bpm.editableText.toString().toInt()
                                        var maxlength = 60000L / i.toLong()
                                        if (i != 0 && i < 301 && i > 0) {

                                            if (System.currentTimeMillis() > (curtime + maxlength)) {
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
                                                            setCurrentStep(0)
                                                        } else {
                                                            setCurrentStep(getCurrentStep() + 1)
                                                        }

                                                    }
                                                    1 -> {
//                                                    curtime = System.currentTimeMillis()
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
                                                            setCurrentStep(0)
                                                        } else {
                                                            setCurrentStep(getCurrentStep() + 1)
                                                        }

                                                    }
                                                }
                                                curtime = System.currentTimeMillis()
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
                                setCurrentStep(getCurrentStep() + 1)
//                                setCurrentStep(0)
                            }

                        }
                    }
                } else {
                    isPlaying = false
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
            //                        if (bpm.editableText.toString() != "" && bpm.editableText.toString() > 10.toString() && bpm.editableText.toString() > 9.toString() && bpm.editableText.toString() > 8.toString() && bpm.editableText.toString() > 7.toString() && bpm.editableText.toString() > 6.toString() && bpm.editableText.toString() > 5.toString() && bpm.editableText.toString() > 4.toString() && bpm.editableText.toString() > 3.toString() && bpm.editableText.toString() > 2.toString() && bpm.editableText.toString() > 1.toString() && bpm.editableText.toString() > 0.toString()) {

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

        val bpContainer = findViewById<LinearLayout>(R.id.beatPatternLayout)
        drawBP(bpContainer)

        val listPreset: MutableList<String> = mutableListOf()
//        val listPreset: MutableList<String> = mutableListOf()


        var presetBut = findViewById<Button>(R.id.preset)
        presetBut.setOnClickListener {
            //            Log.d("PRESET", "Before: " + PresetList.toString())
//
//            delPreset(PresetList, 0)
//            Log.d("PRESET", "After: " + PresetList.toString())

            setTheme(R.style.DarkAppTheme)
            recreate()

        }


        val layoutManager = GridLayoutManager(this, 2)
        layoutManager.orientation = GridLayoutManager.VERTICAL
        PresetLayout.layoutManager = layoutManager

        val adapter = PresetAdapter(this, PresetList)
        PresetLayout.adapter = adapter

        val fab = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        fab.setOnClickListener {
            var cbeatpattern = beatpattern.toMutableList()
            var current = Preset(bpm.editableText.toString().toInt(), cbeatpattern)
            addPreset(PresetList, current)
            adapter.notifyItemInserted(PresetList.size)
            Log.d("PRESET", PresetList.toString())
        }


    }

    fun drawBP(bpContainer: LinearLayout) {
        val pref = applicationContext.getSharedPreferences("appPref", 0)
        val editor = pref.edit()

        var lparams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        lparams.setMargins(8, 0, 8, 0)

        for (i in beatpattern.indices) {

            when (beatpattern.elementAt(i)) {
                0 -> {
                    val btn = Button(this)
                    btn.id = i
                    btn.setBackgroundColor(pref.getInt("main_colour", Color.RED))
                    btn.layoutParams = lparams
                    val index = i
                    btn.setOnClickListener(object : View.OnClickListener {
                        override fun onClick(v: View) {
                            when (beatpattern.elementAt(i)) {
                                0 -> {
                                    Log.d("TAG", "The index is$index , change to TOCK")
                                    btn.setBackgroundColor(pref.getInt("off_colour", Color.BLUE))
                                    beatpattern.set(i, 1)
                                }
                                1 -> {
                                    Log.d("TAG", "The index is$index , change to TICK")
                                    btn.setBackgroundColor(pref.getInt("main_colour", Color.RED))
                                    beatpattern.set(i, 0)
                                }
                            }
                            Log.d("BEAT_PATTERN", "contents: " + beatpattern.toString())
                        }
                    })
//                    btn.setOnLongClickListener{_,isChecked TODO fix this
//                        beatpattern.removeAt(i)
//                        bpContainer.removeView(btn)
//                        true
//                    }
                    bpContainer.addView(btn)
                }
                1 -> {
                    val btn = Button(this)
                    btn.id = i
                    btn.text = i.toString()

                    btn.setBackgroundColor(pref.getInt("off_colour", Color.BLUE))
                    btn.layoutParams = lparams
                    val index = i
                    btn.setOnClickListener(object : View.OnClickListener {
                        override fun onClick(v: View) {
                            when (beatpattern.elementAt(i)) {
                                0 -> {
                                    Log.d("TAG", "The index is$index , change to TOCK")
                                    btn.setBackgroundColor(pref.getInt("off_colour", Color.BLUE))
                                    beatpattern.set(i, 1)
                                }
                                1 -> {
                                    Log.d("TAG", "The index is$index , change to TICK")
                                    btn.setBackgroundColor(pref.getInt("main_colour", Color.RED))
                                    beatpattern.set(i, 0)
                                }
                            }
                            Log.d("BEAT_PATTERN", "contents: " + beatpattern.toString())
                        }
                    })
                    bpContainer.addView(btn)
                }
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

    fun delPreset(PresetList: MutableList<Preset>, index: Int) {
        PresetList.removeAt(index)
    }

    fun drawPreset(PresetList: MutableList<Preset>) {
        for (i in PresetList.indices) {

        }

    }

    override fun onResume() {
        val pref = applicationContext.getSharedPreferences("appPref", 0)
        val editor = pref.edit()
        val bpm = findViewById<EditText>(R.id.bpm)
        bpm.setText(pref.getInt("TapBpm", 120).toString())
//        editor.putInt("TapBpm", 120).apply()
//        recreate()

        super.onResume()
    }

}
