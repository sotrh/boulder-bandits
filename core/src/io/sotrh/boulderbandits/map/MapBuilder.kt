package io.sotrh.boulderbandits.map

/**
 * author: benjamin
 * date: 7/25/16
 * project: BoulderBandits
 * package: io.sotrh.boulderbandits.map
 */
class MapBuilder(val size:Int) {
    private val map:Map by lazy { Map(size) }

    fun checker():MapBuilder {
        val clampedSize = size - 1
        for (x in 0..clampedSize) {
            for (y in x%2..clampedSize step 2) {
                map[x.toFloat(), y.toFloat()].type = 1
            }
        }
        return this
    }

    fun build():Map = map
}