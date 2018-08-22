package me.lesovoy.metronome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.ToggleButton
import kotlinx.coroutines.experimental.cancel
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.util.concurrent.TimeUnit


class Preset {
//    bpm :Int
//    beatpattern :List<Byte>


}

class MainActivity : AppCompatActivity() {

    //    var preset: Preset
    var beatpattern = listOf(0, 1, 1, 1)
    // 0 - Tick 1 - Tock
    private var totalSteps = beatpattern.size - 1 //TODO: -1?
    private var currentStep = 0;
    var isPlaying = false


    public fun setCurrentStep(i: Int) {
        currentStep = i
    }

    public fun getCurrentStep(): Int {
        return currentStep
    }

    public fun setTotalSteps(i: Int) {
        totalSteps = i
    }

    public fun getTotalSteps(): Int {
        return totalSteps
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.app_toolbar))

        val bpm = findViewById<EditText>(R.id.bpm)

        //titlebar
//      val titlebar = findViewById<Toolbar>(R.id.app_toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        //playpause button
        val playpause = findViewById<ToggleButton>(R.id.playpause)
        playpause.setOnCheckedChangeListener { _, isChecked ->
            run {
                //TODO proper listener
                if (isChecked) {
                    Toast.makeText(this, "checked", Toast.LENGTH_SHORT).show()
                    isPlaying = true

                    val vibrator: Vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    var weakVibration: VibrationEffect = VibrationEffect.createOneShot(10, VibrationEffect.DEFAULT_AMPLITUDE);
                    var strongVibration: VibrationEffect = VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE);
//                    val mp :MediaPlayer =

                    launch {

                        while (isPlaying) {
                            if (bpm.editableText.toString() != "") {
                                var i = bpm.editableText.toString().toInt()
                                if (i != 0 && i < 500 && i > 0) {
                                    when (beatpattern.elementAt(currentStep).toInt()) {
                                        0 -> {
                                            //Tick code
                                            vibrator.vibrate(strongVibration)
                                        }
                                        1 -> {
                                            //Tock code
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

            if (bpm.editableText.toString() != "" && i < 10.toString()){
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


//        val testb = findViewById<Button>(R.id.testb)
//        testb.setOnClickListener {
//            testt.text = "current " + getCurrentStep() + " total " + getTotalSteps() + " List " + beatpattern.toString()
//        }
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

