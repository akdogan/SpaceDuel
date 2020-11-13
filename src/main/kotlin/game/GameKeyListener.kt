package game

import helper.logger
import java.awt.event.KeyEvent
import java.awt.event.KeyListener

class GameKeyListener : KeyListener {
    var gameKeyList: MutableList<GameKeyEvent> = mutableListOf()

    override fun keyTyped(e: KeyEvent?) {}

    override fun keyPressed(e: KeyEvent?) {
        logger("keyPressed $e", false)
        val k = gameKeyList.find{ it.isActive && it.gameKeyCode == e?.keyCode}
        k?.pressGameAction?.invoke()

    }

    override fun keyReleased(e: KeyEvent?) {
        logger("keyReleased $e", false)
        val k = gameKeyList.find{ it.isActive && it.gameKeyCode == e?.keyCode}
        k?.releaseGameAction?.invoke()
    }

    fun addEvent(event: GameKeyEvent) = gameKeyList.add(event)
    fun addEvents(events: List<GameKeyEvent>) = gameKeyList.addAll(events)
    fun removeAllEvents() = gameKeyList.removeAll { true }

    private fun switchEvents(name: String, state: Boolean){
        if (name.isEmpty()){
            gameKeyList.forEach { it.isActive = state }
        }
        else {
            gameKeyList.forEach {
                if (it.playerName == name) {
                    it.isActive = state
                    println("switched: ${it.playerName} to $state")
            } }
        }
    }

    fun activateEvents(name: String = "") = switchEvents(name, true)
    fun deactivateEvents(name: String = "") {
        switchEvents(name, false)
        println("events deactivated: $name")
    }
}

class GameKeyEvent(
    val playerName: String,
    val gameKeyCode: Int,
    val pressGameAction: () -> Unit,
    val releaseGameAction: () -> Unit,
    var isActive: Boolean = false
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