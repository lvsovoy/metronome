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
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.launch


class Preset {
//    bpm :Int
//    beatpattern :List<Byte>
}

@Suppress("NAME_SHADOWING")
class MainActivity : AppCompatActivity() {

    //    var preset: Preset
    var beatpattern = mutableListOf(0, 1, 1, 1)
    // 0 - Tick 1 - Tock
    private var totalSteps = beatpattern.size - 1
    private var currentStep = 0
    var isPlaying = false

//    var prevPress = 0L


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

                    setCurrentStep(0)
                    var curtime = System.currentTimeMillis()

                    launch {
                        while (isPlaying) {

                            if (bpm.editableText.toString() != "") {
                                var i = bpm.editableText.toString().toInt()
                                if (i != 0 && i < 301 && i > 0) {
                                    var maxlength = 60000L / bpm.editableText.toString().toLong()
                                    if (System.currentTimeMillis() == curtime + maxlength) {
                                        when (beatpattern.elementAt(currentStep).toInt()) {
                                            0 -> {
                                                curtime = System.currentTimeMillis()
                                                sp.play(tick, 1f, 1f, 0, 0, 1f)
//                                                tg.startTone(3, 10)
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
                                                curtime = System.currentTimeMillis()
                                                sp.play(tock, 1f, 1f, 0, 0, 1f)
//                                                tg.startTone(1, 10)
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
                                    }
                                    //List rotation logic

//                                    delay((60000L / i) - 100, TimeUnit.MILLISECONDS)

                                }
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
            if (bpm.editableText.toString() >= 0.toString()) {
                Toast.makeText(this, "+10", Toast.LENGTH_SHORT).show()
                var i = bpm.editableText.toString().toInt() + 10
                bpm.setText(i.toString())
                true

            } else {
                Toast.makeText(this, "+10", Toast.LENGTH_SHORT).show()
                bpm.setText(10.toString())
                true
            }
        }

        plus.setOnClickListener {
            if (bpm.editableText.toString() >= 0.toString()) {
                var i = bpm.editableText.toString().toInt() + 1
                bpm.setText(i.toString())
            } else {
                bpm.setText(1.toString())
            }
        }


        val minus = findViewById<Button>(R.id.minus)
        var i = bpm.editableText.toString()
        minus.setOnLongClickListener {
            //            if (bpm.editableText.toString() != "" && bpm.editableText.toString() > 10.toString() && bpm.editableText.toString() > 9.toString() && bpm.editableText.toString() > 8.toString() && bpm.editableText.toString() > 7.toString() && bpm.editableText.toString() > 6.toString() && bpm.editableText.toString() > 5.toString() && bpm.editableText.toString() > 4.toString() && bpm.editableText.toString() > 3.toString() && bpm.editableText.toString() > 2.toString() && bpm.editableText.toString() > 1.toString() && bpm.editableText.toString() > 0.toString()) {

            if (bpm.editableText.toString() != "" && i < 10.toString()) {
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
            if (bpm.editableText.toString() != "" && bpm.editableText.toString() > 0.toString()) {
                var i = bpm.editableText.toString().toInt() - 1
                bpm.setText(i.toString())
            } else {
                Toast.makeText(this, "cant be < 0", Toast.LENGTH_SHORT).show()
            }


        }

        val timebutton = findViewById<Button>(R.id.time_button)
        timebutton.setOnClickListener {
            val tapTempo = AlertDialog.Builder(this@MainActivity)
            tapTempo.setView(R.layout.tap_tempo)
            val view = layoutInflater.inflate(R.layout.tap_tempo, null)
            val tapBtn = view.findViewById<Button>(R.id.tap_btn)
            tapTempo.setPositiveButton("SET TEMPO") { dialog, which ->
            }
//            tapBtn.setOnClickListener {
//
//            }
            val dialog: AlertDialog = tapTempo.create()
            dialog.show()
            //            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            //
            //                when (event?.action) {
            //                    MotionEvent.ACTION_DOWN -> {
            ////
            //                        Log.d("OnTouchListener", "ACTION_DOWN")
            //                    }
            //                    MotionEvent.ACTION_UP -> {
            //                        val currPress = System.currentTimeMillis()
            //                        if ((60000 / (currPress - prevPress)) > 0) {
            //                            bpm.setText((60000 / (currPress - prevPress)).toString())
            ////                        Log.d("OnTouchListener", "ACTION_UP prev:" + prevPress + " this: " + currPress + " bpm: " + (60000 / (currPress - prevPress)))
            //
            //                        } else {
            //                            bpm.setText(120.toString())
            //                        }
            //                        prevPress = currPress
            //                    }
            //                }
            //                return v?.onTouchEvent(event) ?: true
            //            }
        }

//        R.id.TapTempo ->


        val bpContainer = findViewById<LinearLayout>(R.id.beatPatternLayout)

        drawBP(bpContainer)


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
            R.id.tapTempo ->{
                val tapIntent = Intent(this, TapTempo::class.java)
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


}


