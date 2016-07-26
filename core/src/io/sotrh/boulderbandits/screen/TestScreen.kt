package io.sotrh.boulderbandits.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import io.sotrh.boulderbandits.map.MapBuilder
import io.sotrh.boulderbandits.map.contains
import io.sotrh.boulderbandits.util.MOVEMENT_SPEED
import io.sotrh.boulderbandits.util.doRender
import io.sotrh.boulderbandits.util.p2m
import io.sotrh.boulderbandits.util.top

class TestScreen : BaseScreen() {

    private var width = 0f
    private var height = 0f

    private lateinit var camera:OrthographicCamera
    private lateinit var render:ShapeRenderer
    private lateinit var batch:SpriteBatch
    private lateinit var font:BitmapFont

    private var map = lazy {
        MapBuilder(16).checker().build()
    }.value

    override fun show() {
        camera = OrthographicCamera()
        render = ShapeRenderer()
        batch = SpriteBatch()
        font = BitmapFont()
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
        processInput(delta)

        // render the map
        render.doRender(camera) {
            map.cells.forEach {
                it.forEach {
                    if (it in camera) {
                        color = if (it.type == 1) Color.WHITE else Color.PURPLE
                        rect(it.x, it.y, 1f, 1f)
                    }
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

        // render the fps
        batch.doRender {
            font.draw(this, "FPS: ${Gdx.graphics.framesPerSecond}", 10f, 10f.top())
        }
    }

    private fun processInput(delta: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit()

        // Camera movements
        camera.position.apply {
            val speed = delta * MOVEMENT_SPEED
            if (Gdx.input.isKeyPressed(Input.Keys.W)) y += speed
            if (Gdx.input.isKeyPressed(Input.Keys.S)) y -= speed
            if (Gdx.input.isKeyPressed(Input.Keys.D)) x += speed
            if (Gdx.input.isKeyPressed(Input.Keys.A)) x -= speed
        }
        camera.update()
    }

    override fun dispose() {
        render.dispose()
    }

}