package launch

import drawing.BackgroundStars
import game.GameConnector
import game.GameKeyEvent
import game.GameKeyListener
import game.State
import helper.*
import java.awt.Color
import javax.swing.JFrame



fun main() {
    // Create Frame
    val frame = JFrame("Space Duel")
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT)
    frame.isVisible = true
    frame.isResizable = false
    val gameLauncher = GameLauncher(frame)



    //val gameConnector = GameConnector(frame)

}

class GameLauncher(val frame: JFrame){
    private val backgroundStars = BackgroundStars()
    private val launchCanvas: LaunchCanvas = LaunchCanvas(
            WINDOW_WIDTH,
            WINDOW_HEIGHT + HUD_HEIGHT,
            backgroundStars
    )
    private val keyListener : GameKeyListener



    init {
        launchCanvas.background = Color.BLACK
        frame.add(launchCanvas)
        frame.pack()

        keyListener = GameKeyListener()
        keyListener.addEvent(GameKeyEvent(
                SYSTEM_KEY_EVENT_NAME,
                START_RESTART,
                ::startGame,
                {},
                State.ACTIVE)
        )
        frame.addKeyListener(keyListener)
    }

    private fun startGame(){
        frame.remove(launchCanvas)
        val gameConnector = GameConnector(frame, keyListener, backgroundStars)
    }
}


