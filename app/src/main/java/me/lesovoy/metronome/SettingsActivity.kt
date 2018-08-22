package me.lesovoy.metronome

import android.app.Fragment
import android.os.Bundle
import android.preference.PreferenceFragment
import android.preference.PreferenceScreen
import android.support.constraint.R.attr.content
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import java.text.AttributedCharacterIterator


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        setSupportActionBar(findViewById(R.id.settings_toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


//        if (fragmentManager.findFragmentById(android.R.id.content) == null) {
//            fragmentManager.beginTransaction()
//                    .add(android.R.id.content, SettingsFragment()).commit()
////            val frag = fragmentManager.findFragmentById(android.R.id.content)
//        }
    }


//    class SettingsFragment : PreferenceFragment() {
//        override fun onCreate(savedInstanceState: Bundle?) {
//            super.onCreate(savedInstanceState)
//            addPreferencesFromResource(R.xml.preferences)
//        }
//    }
}

