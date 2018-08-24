package me.lesovoy.metronome

//import com.sun.org.apache.xerces.internal.util.DOMUtil.getParent
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.SoundPool
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.tap_tempo.*
import kotlinx.coroutines.experimental.cancel
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.util.concurrent.TimeUnit


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

    var prevPress = 0L
    var tbpm = 120


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


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.app_toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val pref = applicationContext.getSharedPreferences("appPref", 0)
        val editor = pref.edit()

        val bpm = findViewById<EditText>(R.id.bpm)

        //playpause button
        val playpause = findViewById<ToggleButton>(R.id.playpause)
        playpause.setOnCheckedChangeListener { _, isChecked ->
            run {
                if (isChecked) {
                    isPlaying = true

                    val vibrator: Vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    var weakVibration: VibrationEffect = VibrationEffect.createOneShot(pref.getInt("weak_vibration", 1).toLong(), VibrationEffect.DEFAULT_AMPLITUDE)
                    var strongVibration: VibrationEffect = VibrationEffect.createOneShot(pref.getInt("strong_vibration", 50).toLong(), VibrationEffect.DEFAULT_AMPLITUDE)
//                    val tickSound = MediaPlayer.create(this, R.raw.tick)
//                    val tockSound = MediaPlayer.create(this, R.raw.tock)
//                    tickSound.isLooping = false
//                    tockSound.isLooping = false
//                    tickSound.prepare()
//                    tockSound.prepare()

                    val sp = SoundPool.Builder().setMaxStreams(2).build()
                    var tick = sp.load(this, R.raw.tick_w, 1)
                    var tock = sp.load(this, R.raw.tock_w, 1)



                    launch {

                        while (isPlaying) {
//                            tickSound.seekTo(0)
//                            tockSound.seekTo(0 )

                            if (bpm.editableText.toString() != "") {
                                var i = bpm.editableText.toString().toInt()
                                if (i != 0 && i < 500 && i > 0) {
                                    when (beatpattern.elementAt(currentStep).toInt()) {
                                        0 -> {
                                            //Tick code
                                            launch {
                                                sp.play(tick, 1f, 1f, 0, 0, 1f)
                                            }
                                            vibrator.vibrate(strongVibration)
                                        }
                                        1 -> {
                                            //Tock code
                                            launch {
                                                sp.play(tock, 1f, 1f, 0, 0, 1f)
                                            }
                                            vibrator.vibrate(weakVibration)
                                        }
                                    }
                                    //List rotation logic
                                    if (currentStep >= totalSteps) {
                                        setCurrentStep(0)
                                    } else {
                                        setCurrentStep(getCurrentStep() + 1)
                                    }

                                    delay((60000L / i - 1L), TimeUnit.MILLISECONDS)
                                } else {
//                                    Toast.makeText(content, "Illegal bpm", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        kotlin.coroutines.experimental.coroutineContext.cancel()

                    }

                } else {
                    Toast.makeText(this, "not checked", Toast.LENGTH_SHORT).show()
                    isPlaying = false


                }
//            Toast.makeText(this, "checked", Toast.LENGTH_SHORT).show()
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
        timebutton.setOnClickListener { it ->
            val tapTempo = AlertDialog.Builder(this@MainActivity)
            tapTempo.setView(R.layout.tap_tempo)
            val view = layoutInflater.inflate(R.layout.tap_tempo, null)
//            layoutInflater.inflate(R.layout.tap_tempo,null)

            tapTempo.setPositiveButton("SET TEMPO") { dialog, which ->
                bpm.setText(tbpm.toString())
            }
            val dialog: AlertDialog = tapTempo.create()

//            val tapBtn = view.findViewById<Button>(R.id.tap_btn)
//            tapBtn.setOnTouchListener { dialog, which ->
//                Toast.makeText(this, "test2", Toast.LENGTH_LONG).show()
//                true
//            }

            dialog.show()


////
//            tapBtn.setOnClickListener{
//                val currPress = System.currentTimeMillis()
//                if ((60000 / (currPress - prevPress)) > 0) {
//                    tapBpn.text=(60000 / (currPress - prevPress)).toString()
//                    tbpm = tapBpm.text.toString().toInt()
//
////                        Log.d("OnTouchListener", "ACTION_UP prev:" + prevPress + " this: " + currPress + " bpm: " + (60000 / (currPress - prevPress)))
//
//                } else {
//                    tapBpm.setText(120.toString())
//                    tbpm = tapBpm.text.toString().toInt()
//                }
//                prevPress = currPress
//        }


//FIXME onclick listener

        }

        val bpContainer = findViewById<LinearLayout>(R.id.beatPatternLayout)

        var lparams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        lparams.setMargins(16, 0, 0, 0)
//        lparams.height = 32
//        lparams.width = 32

        val mainColour = pref.getInt("main_colour", Color.RED)
//                val offColour =

        for (i in beatpattern.indices) {

            when (beatpattern.elementAt(i)) {
                0 -> {
                    val btn = Button(this)
                    btn.id = i
                    btn.text = i.toString()
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

    fun launchSettingsActivity() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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

