package me.lesovoy.metronome

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.tap_tempo.*

class TapTempo : AppCompatActivity() {
    var prevPress = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tap_tempo)
        val tap_btn = findViewById<Button>(R.id.tap_btn)
        tap_btn.setOnClickListener {

            val currPress = System.currentTimeMillis()
            if ((60000 / (currPress - prevPress)) > 0) {
                val tap_bpm = findViewById<TextView>(R.id.tap_bpm)
                tap_bpm.setText((60000 / (currPress - prevPress)).toString())
//                        Log.d("OnTouchListener", "ACTION_UP prev:" + prevPress + " this: " + currPress + " bpm: " + (60000 / (currPress - prevPress)))

            } else {
                tap_bpm.setText(120.toString())
            }
            prevPress = currPress

        }

    }
}
