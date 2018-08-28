package me.lesovoy.metronome

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.tap_tempo.*
import android.content.Intent




class TapTempo : AppCompatActivity() {
    var prevPress = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tap_tempo)

        setSupportActionBar(findViewById(R.id.tap_toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val pref = applicationContext.getSharedPreferences("appPref", 0)
        val editor = pref.edit()

        val tap_btn = findViewById<Button>(R.id.tap_btn)
        tap_btn.setOnClickListener {

            val currPress = System.currentTimeMillis()
            if ((60000 / (currPress - prevPress)) > 0) {
                val tap_bpm = findViewById<TextView>(R.id.tap_bpm)
                tap_bpm.text = (60000 / (currPress - prevPress)).toString()
//                        Log.d("OnTouchListener", "ACTION_UP prev:" + prevPress + " this: " + currPress + " bpm: " + (60000 / (currPress - prevPress)))
            } else {
                tap_bpm.text = 120.toString()
            }
            prevPress = currPress
            editor.putInt("TapBpm", tap_bpm.text.toString().toInt()).apply()
        }
    }
}
