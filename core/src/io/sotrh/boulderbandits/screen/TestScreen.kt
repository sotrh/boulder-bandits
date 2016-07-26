package io.sotrh.boulderbandits.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import io.sotrh.boulderbandits.map.Map
import io.sotrh.boulderbandits.map.contains
import io.sotrh.boulderbandits.util.doRender
import io.sotrh.boulderbandits.util.p2m

class TestScreen : BaseScreen() {

    private var width = 0f
    private var height = 0f
    private var camera = lazy { OrthographicCamera() }.value
    private var render = lazy { ShapeRenderer() }.value
    private var map = lazy { Map(16) }.value

    override fun show() {

    }

    override fun resize(width: Int, height: Int) {
        this.width = width.p2m()
        this.height = height.p2m()
        setupCamera()
    }

    private fun setupCamera() {
        camera.setToOrtho(false, width, height)
    }

    override fun render(delta: Float) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        camera.update()

        render.doRender(camera) {
            color = Color.WHITE
            map.cells.forEach {
                it.forEach {
                    if (it in camera) rect(it.x, it.y, 1f, 1f)
                }
            }
        }.doRender(camera, ShapeRenderer.ShapeType.Line) {
            color = Color.BLACK
            map.cells.forEach {
                it.forEach {
                    if (it in camera) rect(it.x, it.y, 1f, 1f)
                }
            }
        }
    }

    override fun dispose() {
        render.dispose()
    }

}