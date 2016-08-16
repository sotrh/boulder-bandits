package io.sotrh.boulderbandits.util

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

/**
 * author: benjamin
 * date: 7/23/16
 * project: BoulderBandits
 * package: io.sotrh.boulderbandits.util
 */

// number conversions
fun Int.p2m(scale:Float = PIXELS_TO_METERS) = this / scale
fun Int.m2p(scale:Float = PIXELS_TO_METERS) = this * scale
fun Float.p2m(scale:Float = PIXELS_TO_METERS) = this / scale
fun Float.m2p(scale: Float = PIXELS_TO_METERS) = this * scale

fun Int.f() = this.toFloat()
fun Float.i() = this.toInt()

// spacing extensions
fun Float.top() = Gdx.graphics.height - this
fun Float.right() = Gdx.graphics.width - this

// shape renderer extensions
inline fun ShapeRenderer.doRender(shapeType:ShapeRenderer.ShapeType, block:ShapeRenderer.()->Unit): ShapeRenderer {
    begin(shapeType)
    block()
    end()
    return this
}
inline fun ShapeRenderer.doRender(camera: Camera, shapeType: ShapeRenderer.ShapeType = ShapeRenderer.ShapeType.Filled, block: ShapeRenderer.() -> Unit): ShapeRenderer {
    projectionMatrix = camera.combined
    return doRender(shapeType, block)
}
inline fun SpriteBatch.doRender(block:SpriteBatch.()->Unit): SpriteBatch {
    begin()
    block()
    end()
    return this
}