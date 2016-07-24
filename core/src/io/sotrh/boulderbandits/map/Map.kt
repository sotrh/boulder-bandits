package io.sotrh.boulderbandits.map

/**
 * author: benjamin
 * date: 7/23/16
 * project: BoulderBandits
 * package: io.sotrh.boulderbandits.map
 */
class Map(val size:Int, val x:Float=0f, val y:Float=0f) {

    val cells:Array<Array<Cell>>

    init {
        var x = 0f
        cells = Array(size) {
            var y = 0f
            val array = Array(size) { Cell(0, x, y++) }
            x++
            array
        }
    }

    operator fun get(x: Float, y: Float): Cell = cells[(this.x - x).toInt()][(this.y - y).toInt()]
//    fun contains(x: Float, y: Float): Boolean = (x >= this.x) && (y >= this.y) && (x <= this.x + size) && (y <= this.y + size)

    inner class Cell(val type:Int, val x:Float, val y:Float) {

    }
}