package me.lesovoy.metronome

class Strokes() {

    var loops: Int = 0
    var currentBeat: Int = 1
    var currentLoop: Int = 0
    var currentStroke: Int = 0


    var strokes = mutableListOf<List<Int>>()

    /*
    public fun addStrokes(pattern: List<Integer>) {
        addStrokes(0.toInt(), pattern)
    }
   */
    public fun addStrokes(count: Int, pattern: List<Int>) {
        var tmp = mutableListOf<Int>()
        tmp.add(count)
        tmp.addAll(pattern)
        strokes.add(tmp)
        //println("strokes:" + strokes)
    }


    fun getBeat(): Int {
        //println("before: currentStroke:" + currentStroke + " currentLoop:" + currentLoop + " currentBeat:" + currentBeat)
        var beatPattern = strokes.get(currentStroke)
        loops = beatPattern.get(0)
        var beat = beatPattern.get(currentBeat)
        currentBeat++
        if (currentBeat == beatPattern.size) {
            currentBeat = 1
            currentLoop++
        }
        if (currentLoop == loops) {
            currentLoop = 0
            currentStroke++
            if (currentStroke == strokes.size)
                currentStroke = 0
        }

        //println("after: currentStroke:" + currentStroke + " currentLoop:" + currentLoop + " currentBeat:" + currentBeat)
        return beat
    }


}