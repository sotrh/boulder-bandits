package io.sotrh.boulderbandits.util

import com.badlogic.gdx.graphics.Camera
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

// shape renderer extensions
fun ShapeRenderer.doRender(shapeType:ShapeRenderer.ShapeType, block:ShapeRenderer.()->Unit) {
    begin(shapeType)
    block()
    end()
}
fun ShapeRenderer.doRender(camera: Camera, shapeType: ShapeRenderer.ShapeType = ShapeRenderer.ShapeType.Filled, block: ShapeRenderer.() -> Unit) {
    projectionMatrix = camera.combined
    doRender(shapeType, block)
}