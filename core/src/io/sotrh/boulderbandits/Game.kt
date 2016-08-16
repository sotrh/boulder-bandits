package io.sotrh.boulderbandits

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import io.sotrh.boulderbandits.screen.Screens

class Game : Game() {
    override fun create() {
        Gdx.graphics.setFullscreenMode(Gdx.graphics.displayMode)
        setScreen(Screens.TEST.screen)
    }
}
