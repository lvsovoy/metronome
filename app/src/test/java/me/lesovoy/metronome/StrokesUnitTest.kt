package me.lesovoy.metronome

import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class StrokesUnitTest {
    @Test
    fun testBeats() {
        var testStrokes = Strokes()
        testStrokes.addStrokes(1, mutableListOf(1, 0, 0, 0))
        for (item: Int in 0..20) {
            println(testStrokes.getBeat())
        }

    }
}
