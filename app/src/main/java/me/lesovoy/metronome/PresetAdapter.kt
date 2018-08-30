package me.lesovoy.metronome

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.preset_card.view.*
import me.lesovoy.metronome.R.id.BeatPatternString
import me.lesovoy.metronome.R.id.PresetBpm


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
                val bpm = PresetBpm
                val pattern = BeatPatternString
                Log.d("bpm", bpm.toString())
                Log.d("pattern", pattern.toString())


                // SET DATA TO PRESET

            }
            itemView.removePresetButton.setOnClickListener {
                //DELETE CURRENT PRESET
                presetList.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)


            }
        }

        fun setData(preset: Preset, position: Int) {

            itemView.PresetBpm.text = preset.pbpm.toString()
            //String gigamajigery
            var string = preset.pbeatpattern.toString()
            string = string.substring(1, string.length - 1)
            string = string.replace(",", "")

            val colortick: Int = 0


//            string = string.replace("1","<font color =\"#f442aa\">&#x2022;</font>")
//            string = string.replace("1","<font color =\"#f442aa\">&#x2022;</font>")

            string = string.replace("0", "x")
            string = string.replace("1", "&#x2022;")


            itemView.BeatPatternString.text = Html.fromHtml(string, 0)
        }
    }
}