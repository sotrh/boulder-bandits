package io.sotrh.boulderbandits.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import io.sotrh.boulderbandits.camera.OrbitCamera
import io.sotrh.boulderbandits.map.Map
import io.sotrh.boulderbandits.map.MapBuilder
import io.sotrh.boulderbandits.util.MOVEMENT_SPEED
import io.sotrh.boulderbandits.util.doRender
import io.sotrh.boulderbandits.util.f
import io.sotrh.boulderbandits.util.top

class TestScreen : BaseScreen() {

    private var width = 0f
    private var height = 0f

    private lateinit var map:Map

    // 2d stuff
    private lateinit var batch:SpriteBatch
    private lateinit var font:BitmapFont

    // 3d camera stuff
    private lateinit var playerPosition:Vector3
    private lateinit var camera:OrbitCamera

    // 3d stuff
    private lateinit var environment: Environment
    private lateinit var modelBatch:ModelBatch
    private lateinit var boxModel:Model
    private lateinit var boxInstance:ModelInstance
    private lateinit var groundModel:Model
    private lateinit var groundInstance:ModelInstance

    override fun show() {
        modelBatch = ModelBatch()
        camera = OrbitCamera()
        batch = SpriteBatch()
        font = BitmapFont()
        map = MapBuilder(16).checker().build()

        playerPosition = Vector3(map.size * 0.5f, 0f, map.size * 0.5f)

        createModels()
        createEnvironment()
    }

    private fun createModels() {
        ModelBuilder().apply {
            boxModel = createBox(1f, 1f, 1f,
                    Material(ColorAttribute.createDiffuse(Color.PURPLE)),
                    (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal).toLong()
            )
            boxInstance = ModelInstance(boxModel)

            groundModel = createRect(
                    0f, 0f, 0f, // v0 bot right
                    0f, 0f, 1f, // v1 top right
                    1f, 0f, 1f, // v2 top left
                    1f, 0f, 0f, // v4 bot left
                    0f, 1f, 0f,
                    Material(ColorAttribute.createDiffuse(Color.WHITE)),
                    (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal).toLong()
            )
            groundInstance = ModelInstance(groundModel)
        }
    }

    private fun createEnvironment() {
        environment = Environment()
        environment.set(ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f))
        environment.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f))
    }

    override fun resize(width: Int, height: Int) {
        this.width = width.toFloat()
        this.height = height.toFloat()
        setupCamera()
    }

    private fun setupCamera() {
        camera.doUpdate {
            fieldOfView = 60f
            near = 0.1f
            far = 100f
            viewportWidth = width
            viewportHeight = height
            radius = 5.0f
            verticalAngle = MathUtils.PI / 4
            lookAt = playerPosition
        }
    }

    override fun render(delta: Float) {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        processInput(delta)

        modelBatch.begin(camera)
        for (x in 0..map.size-1) {
            for (y in 0..map.size-1) {
                map[x.f(), y.f()].apply {
                    if (type > 0) {
                        boxInstance.transform.setTranslation(Vector3(x - 0.5f, 0.5f, y - 0.5f))
                        boxInstance.calculateTransforms()
                        modelBatch.render(boxInstance, environment)
                    } else {
                        groundInstance.transform.setTranslation(Vector3(x - 1f, 0f, y - 1f))
                        groundInstance.calculateTransforms()
                        modelBatch.render(groundInstance, environment)
                    }
                }
            }
        }
        modelBatch.end()

        // render the fps
        batch.doRender {
            font.draw(this, "FPS: ${Gdx.graphics.framesPerSecond}", 10f, 10f.top())
        }
    }

    private fun processInput(delta: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit()

        // Camera movements
        val speed = delta * MOVEMENT_SPEED
        val movement = Vector2().apply {
            if (Gdx.input.isKeyPressed(Input.Keys.W)) y = -speed
            if (Gdx.input.isKeyPressed(Input.Keys.S)) y = speed
            if (Gdx.input.isKeyPressed(Input.Keys.D)) x = speed
            if (Gdx.input.isKeyPressed(Input.Keys.A)) x = -speed
        }
        val rotation = Vector2().apply {
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) y = speed
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) y = -speed
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) x = -speed
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) x = speed
        }

        camera.doUpdate {
            moveRelative(movement.y, movement.x)
            horizontalAngle += rotation.x
            verticalAngle += rotation.y
        }
    }

    override fun dispose() {
        batch.dispose()
        font.dispose()

        modelBatch.dispose()
        boxModel.dispose()
        groundModel.dispose()
    }

}