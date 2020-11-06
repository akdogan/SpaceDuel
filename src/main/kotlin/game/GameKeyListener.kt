package game

import helper.logger
import java.awt.event.KeyEvent
import java.awt.event.KeyListener

class GameKeyListener(private val gameKeyList: List<GameKeyEvent>) : KeyListener {
    override fun keyTyped(e: KeyEvent?) {

    }

    override fun keyPressed(e: KeyEvent?) {
        logger("keyPressed $e", false)
        val k = gameKeyList.find{ it.gameKeyCode == e?.keyCode}
        k?.pressGameAction?.invoke()

    }

    override fun keyReleased(e: KeyEvent?) {
        logger("keyReleased $e", false)
        val k = gameKeyList.find{ it.gameKeyCode == e?.keyCode}
        k?.releaseGameAction?.invoke()
    }
}

class GameKeyEvent(
    val gameKeyCode: Int,
    val pressGameAction: () -> Unit,
    val releaseGameAction: () -> Unit
)

/*
Keycodes
W       87
A       65
S       83
D       68
Q       81
Space   32
Up      38
left    37
right   39
 */