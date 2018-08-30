package me.lesovoy.metronome

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.beatpattern_button.view.*

class BeatpatternAdapter(val context: Context, val beatPattern: MutableList<Int>) : RecyclerView.Adapter<BeatpatternAdapter.BeatpatternViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeatpatternViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.beatpattern_button, parent, false)
        return BeatpatternViewHolder(view)

    }

    override fun getItemCount(): Int {
        return beatPattern.size

    }

    override fun onBindViewHolder(holder: BeatpatternViewHolder, position: Int) {
        val beat = beatPattern[position]

        holder.setData(beat, position)
    }

    inner class BeatpatternViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var pos = 0

        init {
            itemView.setOnClickListener {
                // SET DATA TO PRESET
//                setPreset(presetList[adapterPosition])
                when (beatPattern.elementAt(adapterPosition)) {
                    0 -> {
                        itemView.setBackgroundColor(Color.BLUE)//(pref.getInt("main_colour", Color.RED))
                        itemView.BeatText.text = Html.fromHtml("&#x2022;", 0)
                        beatPattern.set(adapterPosition, 1)
                    }
                    1 -> {
                        itemView.setBackgroundColor(Color.RED)//(pref.getInt("off_colour", Color.BLUE))
                        itemView.BeatText.text = "x"
                        beatPattern.set(adapterPosition, 0)
                    }
                }
            }
            itemView.setOnLongClickListener {
                //DELETE CURRENT PRESET
                beatPattern.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
                true
            }
        }

        fun setData(beat: Int, position: Int) {

            when (beatPattern.elementAt(adapterPosition)) {
                0 -> {
                    itemView.setBackgroundColor(Color.RED)//(pref.getInt("main_colour", Color.RED))
                    itemView.BeatText.text = "x"
                }
                1 -> {
                    itemView.setBackgroundColor(Color.BLUE)//(pref.getInt("off_colour", Color.BLUE))
                    itemView.BeatText.text = Html.fromHtml("&#x2022;", 0)
                }
            }

        }
    }
}