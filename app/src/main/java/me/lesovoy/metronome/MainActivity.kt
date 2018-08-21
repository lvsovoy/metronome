package me.lesovoy.metronome

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.ToggleButton
import kotlinx.android.synthetic.main.activity_main.*

class Preset {
//    bpm :Int
//    beatpattern :List<Byte>


}

class MainActivity : AppCompatActivity() {

    //    var preset: Preset
    private var beatpattern = listOf(0, 1, 1, 1);
    // 0 - Tick 1 - Tock
    private var totalSteps = beatpattern.size //TODO: -1?
    private var currentStep = 1;

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


    fun tick(beatpattern: List<Int>, totalSteps: Int, currentStep: Int) {

        // Check settings vibration/ display

        //Trigger sound and delay
        when (beatpattern[currentStep]) {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.app_toolbar))

        val bpm = findViewById<EditText>(R.id.bpm)

        //titlebar
//      getSupportActionBar.title = "TEST"


        //playpause button
        val playpause = findViewById<ToggleButton>(R.id.playpause)
        playpause.setOnCheckedChangeListener { _, isChecked ->
            run {
                //TODO proper listener
                if (isChecked) {
                    Toast.makeText(this, "checked", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "not checked", Toast.LENGTH_SHORT).show()
                }
//            Toast.makeText(this, "checked", Toast.LENGTH_SHORT).show()
            }
        }
        //plu/minus buttons
        val plus = findViewById<Button>(R.id.plus)
        plus.setOnClickListener{
            var i = bpm.editableText.toString().toInt() + 1
            bpm.setText(i.toString())
        }

        val minus = findViewById<Button>(R.id.minus)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
//            Launch settings activity
                Toast.makeText(this, "Settigns", Toast.LENGTH_SHORT).show()
            }
            R.id.about -> {
//            Launch about activity
                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show()

            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun ticker() {
        var play = false;



        while (play) {
            tick(beatpattern, totalSteps, currentStep)
        }

    }
}
