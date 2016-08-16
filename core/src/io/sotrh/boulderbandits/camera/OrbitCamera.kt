package io.sotrh.boulderbandits.camera

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector3

/**
 * author: benjamin
 * date: 8/15/16
 * project: BoulderBandits
 * package: io.sotrh.boulderbandits.camera
 */
class OrbitCamera() : PerspectiveCamera() {
    var lookAt = Vector3()
    var radius = 0.0f
    var verticalAngle = 0.0f
    var horizontalAngle = 0.0f

    override fun update() {
        calcPosition()
        super.update()
    }

    fun doUpdate(block: OrbitCamera.()->Unit) {
        block()
        update()
    }

    fun moveRelative(forward:Float, right:Float) {
        val frontDirection = Vector3(
                MathUtils.cos(horizontalAngle),
                0f, // only care about the horizontal angle for now
                MathUtils.sin(horizontalAngle)
        )
        val sideDirection = frontDirection.cpy().apply {
            val temp = -x
            x = z
            z = temp
        }

        frontDirection.scl(forward)
        sideDirection.scl(right)

        lookAt.add(frontDirection).add(sideDirection)
    }

    private fun calcPosition() {
        // calculate position based on look at
        val position = Vector3(
                MathUtils.cos(horizontalAngle),
                MathUtils.sin(verticalAngle),
                MathUtils.sin(horizontalAngle)
        ).scl(radius).add(lookAt)
        this.position.set(position)
        lookAt(lookAt)
    }
}