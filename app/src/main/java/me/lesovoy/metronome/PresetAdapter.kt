package me.lesovoy.metronome

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import kotlinx.android.synthetic.main.preset_card.view.*
import me.lesovoy.metronome.R.id.*

class PresetAdapter(val context: Context, val presetList: MutableList<Preset>) : RecyclerView.Adapter<PresetAdapter.PresetViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PresetViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.preset_card, parent, false)
        return PresetViewHolder(view)

    }

    override fun getItemCount(): Int {
        return presetList.size

    }

    override fun onBindViewHolder(holder: PresetViewHolder, position: Int) {
        val preset = presetList[position]

        holder.setData(preset, position)
    }

    inner class PresetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var pos = 0

        init {
            itemView.setOnClickListener {
                // SET DATA TO PRESET
            }
            itemView.removePresetButton.setOnClickListener {

            }
        }

        fun setData(preset: Preset, position: Int) {
            itemView.PresetBpm.text = preset.pbpm.toString()
            itemView.BeatPatternString.text = preset.pbeatpattern.toString()
        }
    }
}