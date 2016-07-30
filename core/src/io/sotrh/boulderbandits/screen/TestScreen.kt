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
import com.badlogic.gdx.math.Vector3
import io.sotrh.boulderbandits.map.Map
import io.sotrh.boulderbandits.map.MapBuilder
import io.sotrh.boulderbandits.util.MOVEMENT_SPEED
import io.sotrh.boulderbandits.util.doRender
import io.sotrh.boulderbandits.util.top

class TestScreen : BaseScreen() {

    private var width = 0f
    private var height = 0f

    private lateinit var map:Map

    // 2d stuff
    private lateinit var batch:SpriteBatch
    private lateinit var font:BitmapFont

    // 3d camera stuff
    private lateinit var camera:PerspectiveCamera
    private lateinit var cameraController: CameraInputController

    // 3d stuff
    private lateinit var environment: Environment
    private lateinit var modelBatch:ModelBatch
    private lateinit var boxModel:Model
    private lateinit var boxInstance:ModelInstance
    private lateinit var groundModel:Model
    private lateinit var groundInstance:ModelInstance

    override fun show() {
        modelBatch = ModelBatch()
        camera = PerspectiveCamera()
        batch = SpriteBatch()
        font = BitmapFont()
        map = MapBuilder(16).checker().build()

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
                    0f, 0f, 0f,
                    1f, 0f, 0f,
                    1f, 0f, 1f,
                    0f, 0f, 1f,
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
        camera.apply {
            fieldOfView = 60f
            near = 0.1f
            far = 100f
            viewportWidth = width
            viewportHeight = height
            lookAt(map.size * 0.5f, 0f, map.size * 0.5f)
            position.set(0f, 3f, -3f)
        }.update()

        cameraController = CameraInputController(camera)
        Gdx.input.inputProcessor = cameraController
    }

    override fun render(delta: Float) {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        processInput(delta)
        cameraController.update()

        modelBatch.begin(camera)
        for (x in 0..map.size-1) {
            for (y in 0..map.size-1) {
                map[x.toFloat(), y.toFloat()].apply {
                    if (type > 0) {
                        boxInstance.transform.setTranslation(Vector3(x - 0.5f, 0.5f, y - 0.5f))
                        boxInstance.calculateTransforms()
                        modelBatch.render(boxInstance, environment)
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
        batch.dispose()
        font.dispose()

        modelBatch.dispose()
        boxModel.dispose()
        groundModel.dispose()
    }

}