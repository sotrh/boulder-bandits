package io.sotrh.boulderbandits.map

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox

/**
 * author: benjamin
 * date: 7/24/16
 * project: BoulderBandits
 * package: io.sotrh.boulderbandits.map
 */

// camera extension to see if frustrum contains parts of the map
operator fun OrthographicCamera.contains(cell: Map.Cell) : Boolean {
    return frustum.boundsInFrustum(BoundingBox(Vector3(cell.x, cell.y, 0f), Vector3(cell.x + 1, cell.y + 1, 0f)))
}