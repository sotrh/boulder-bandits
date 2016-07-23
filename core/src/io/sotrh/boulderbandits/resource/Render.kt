package io.sotrh.boulderbandits.resource

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

class Render {
    var width = Gdx.graphics.width
    var height = Gdx.graphics.height

    var spriteBatch: SpriteBatch? = null
    var shapeRenderer: ShapeRenderer? = null

    fun init() {
        spriteBatch = SpriteBatch()
        shapeRenderer = ShapeRenderer()
    }

    fun dispose() {
        spriteBatch?.dispose()
        spriteBatch = null
        shapeRenderer?.dispose()
        shapeRenderer = null
    }
}