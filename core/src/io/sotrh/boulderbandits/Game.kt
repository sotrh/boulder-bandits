package io.sotrh.boulderbandits

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import io.sotrh.boulderbandits.screen.Screens

class Game : Game() {

    override fun create() {
        setScreen(Screens.TEST.screen)
    }
}
