package me.lesovoy.metronome

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.EditText




class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.app_toolbar))
        val bmp1 = findViewById<EditText>(R.id.bpm)
    }
    button.setOnClickListener( new View.oClickListener()){
        public void onClick(Viev v){
            int bmp = Integer.parseInt(bmptext.getText().toString().trim());


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
}
