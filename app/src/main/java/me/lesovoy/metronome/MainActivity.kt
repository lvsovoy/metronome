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
    private var totalSteps = beatpattern.size //TODO: -1?
    private var currentStep = 1;
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
                    var effect: VibrationEffect = VibrationEffect.createOneShot(10, VibrationEffect.DEFAULT_AMPLITUDE);


                    launch {
                        while (isPlaying) {
                            var i = bpm.editableText.toString().toInt()
                            if (i != 0) {
                                vibrator.vibrate(effect)
//                                tick(beatpattern,getTotalSteps(),getCurrentStep())
                                delay((60000L / i - 1L), TimeUnit.MILLISECONDS)
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
            Toast.makeText(this, "+10", Toast.LENGTH_SHORT).show()
            var i = bpm.editableText.toString().toInt() + 10
            bpm.setText(i.toString())
            true
        }
        plus.setOnClickListener {
            var i = bpm.editableText.toString().toInt() + 1
            bpm.setText(i.toString())
        }


        val minus = findViewById<Button>(R.id.minus)
        minus.setOnLongClickListener {
            Toast.makeText(this, "-10", Toast.LENGTH_SHORT).show()
            var i = bpm.editableText.toString().toInt() - 10
            bpm.setText(i.toString())
            true
        }
        minus.setOnClickListener {
            var i = bpm.editableText.toString().toInt() - 1
            bpm.setText(i.toString())
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

    fun tick(beatpattern: List<Int>, totalSteps: Int, currentStep: Int) {

        // Check settings vibration/ display

        //Trigger sound and delay
        when (beatpattern.elementAt(currentStep)) {
            0 -> {
                //Tick code
                Toast.makeText(this, "Tick", Toast.LENGTH_SHORT).show()

            }
            1 -> {
                //Tock code
                Toast.makeText(this, "Tock", Toast.LENGTH_SHORT).show()

            }
        }

        //List rotation logic
        if (currentStep == totalSteps) {
            setCurrentStep(0)
        } else {
            setCurrentStep(getCurrentStep() + 1)
        }
    }


}

