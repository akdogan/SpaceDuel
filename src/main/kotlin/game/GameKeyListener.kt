package game

import helper.logger
import java.awt.event.KeyEvent
import java.awt.event.KeyListener

class GameKeyListener : KeyListener {
    var gameKeyList: MutableList<GameKeyEvent> = mutableListOf()

    override fun keyTyped(e: KeyEvent?) {}

    override fun keyPressed(e: KeyEvent?) {
        logger("keyPressed $e", false)
        val k = gameKeyList.find{ it.state == State.ACTIVE && it.gameKeyCode == e?.keyCode}
        k?.pressGameAction?.invoke()
    }

    override fun keyReleased(e: KeyEvent?) {
        logger("keyReleased $e", false)
        val k = gameKeyList.find{ it.state == State.ACTIVE && it.gameKeyCode == e?.keyCode}
        k?.releaseGameAction?.invoke()
    }


    fun addEvent(event: GameKeyEvent) = gameKeyList.add(event)
    fun addEvents(events: List<GameKeyEvent>) = gameKeyList.addAll(events)
    fun removeAllEvents() = gameKeyList.removeAll { true }


    fun switchEventsByName(state: State, vararg names: String){
        gameKeyList.forEach {
            if (names.contains(it.playerName)){
                it.state = state
            }
        }
    }

}

class GameKeyEvent(
    val playerName: String,
    val gameKeyCode: Int,
    val pressGameAction: () -> Unit,
    val releaseGameAction: () -> Unit,
    var state: State = State.INACTIVE
)

enum class State{
    ACTIVE, INACTIVE
}

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