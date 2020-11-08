package game

import drawing.*
import helper.*
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Point
import java.util.*
import javax.swing.JFrame

class GameConnector(frame: JFrame) {
    // Create Player One and Two Ships and KeyEvents
    private val playerOne: Ship = Ship(
        P1_NAME,
        P1_START_POINT,
        P1_START_VECTOR,
        P1_LINE_COLOR,
        P1_FILL_COLOR,
        P1_SHIELD_COLORS
    )
    private  val playerTwo: Ship = Ship(
        P2_NAME,
        P2_START_POINT,
        P2_START_VECTOR,
        P2_LINE_COLOR,
        P2_FILL_COLOR,
        P2_SHIELD_COLORS
    )
    private val playerEvents: List<GameKeyEvent> = listOf(
        GameKeyEvent(P1_NAME, P1_RIGHT, {playerOne.turningClock = true}, {playerOne.turningClock = false}),
        GameKeyEvent(P1_NAME, P1_LEFT, {playerOne.turningCounterClock = true}, {playerOne.turningCounterClock = false}),
        GameKeyEvent(P1_NAME, P1_FIRE, {playerOne.firing = true}, {playerOne.firing = false}),
        GameKeyEvent(P2_NAME, P2_RIGHT, {playerTwo.turningClock = true}, {playerTwo.turningClock = false}),
        GameKeyEvent(P2_NAME, P2_LEFT, {playerTwo.turningCounterClock = true}, {playerTwo.turningCounterClock = false}),
        GameKeyEvent(P2_NAME, P2_FIRE, {playerTwo.firing = true}, {playerTwo.firing = false})
    )

    private val canvas: GameDrawCanvas = GameDrawCanvas(WINDOW_WIDTH, WINDOW_HEIGHT )
    private val hud = GameHudCanvas(WINDOW_WIDTH, HUD_HEIGHT)
    private val keyListener: GameKeyListener
    private val moveTimerTask = MoveTimerTask()
    private val paintTimerTask = PaintTimerTask(::collectAndRepaint)
    private val animationTimerTask = AnimationTimerTask()
    private val timer = Timer()
    private val backgroundStars = BackgroundStars()

    init {
        // add Canvas to Frame
        canvas.background = Color.BLACK
        hud.background = Color.LIGHT_GRAY
        hud.labels = Pair(HudLabel(
            playerOne.name,
            P1_LINE_COLOR,
            SHIELD_MAX_CHARGE
        ), HudLabel(
            playerTwo.name,
            P2_LINE_COLOR,
            SHIELD_MAX_CHARGE
        ))
        frame.add(hud, BorderLayout.NORTH)
        frame.add(canvas, BorderLayout.SOUTH)

        // Create KeyListener and finish configuring frame
        keyListener = GameKeyListener()
        frame.addKeyListener(keyListener)
        frame.pack()

        // Configure Timers
        timer.scheduleAtFixedRate(paintTimerTask, 500, PAINT_TIMER_INTERVAL)
        timer.scheduleAtFixedRate(moveTimerTask, 1000, MOVE_TIMER_INTERVAL)
        moveTimerTask.addElement(playerOne)
        moveTimerTask.addElement(playerOne.cannon)
        moveTimerTask.addElement(playerTwo)
        moveTimerTask.addElement(playerTwo.cannon)
        moveTimerTask.addElement(backgroundStars)
        timer.scheduleAtFixedRate(animationTimerTask,1000, ANIMATION_TIMER_INTERVAL)
        animationTimerTask.addFunction(playerOne.cannon::switchAnimation)
        animationTimerTask.addFunction(playerTwo.cannon::switchAnimation)
        animationTimerTask.addFunction(playerOne.shield::switchAnimation)
        animationTimerTask.addFunction(playerTwo.shield::switchAnimation)
        // GameKeyEvents are added with delay
        // TODO Maybe Better to make the controls so they can be activated and deactivated?
        moveTimerTask.addElement(DelayedExecution(
                { keyListener.gameKeyList = playerEvents.toMutableList() },
                13
        ))

        // Configure Targeting
        playerOne.cannon.getTarget = acquireTarget(playerTwo)
        playerOne.cannon.hitTarget = hitTarget(playerTwo)
        playerTwo.cannon.getTarget = acquireTarget(playerOne)
        playerTwo.cannon.hitTarget = hitTarget(playerOne)
    }

    // Returns a function that returns the given ships center and radius
    private fun acquireTarget(ship: Ship): () -> Triple<Point, Int, String>{
        return { Triple(ship.center, ship.radius, ship.name)}
    }

    // Returns a function that returns the function triggering the other ships shields
    private fun hitTarget(ship: Ship): () -> Unit {
        return {
            // ship.hit() takes a function that should be triggered when the shields are
            // empty and thus the ship was destroyed. Function triggerEndAnimation() ends the game
            ship.hit(::triggerEndAnimation)
        }
    }

    private fun collectAndRepaint(){
        canvas.shapesToDraw = backgroundStars.collectShapes() + playerOne.collectShapes() + playerTwo.collectShapes()
        canvas.debugTest = collectDebugMessages()
        canvas.repaint()
        hud.labels.first.currentShieldCharge = playerOne.shield.powerLeft
        hud.labels.second.currentShieldCharge = playerTwo.shield.powerLeft
        hud.repaint()
    }

    private fun collectDebugMessages(): List<String>{
        return listOf(
            "P1 RC count: ${playerOne.cannon.rechargeCounter}",
            "P1 Shots: ${playerOne.cannon.shots.size}",
            "P1 Shield: ${playerOne.shield.active}",
            "P2 Position: ${playerTwo.centerToString()}",
            "P1 Targeting: ${playerOne.cannon.getTarget?.invoke()}"
        )
    }
    
    private fun triggerEndAnimation(destroyedPlayerName: String, winnerName:String, explosion: Explosion){
        animationTimerTask.addFunction { explosion.switchAnimation() }
        keyListener.removeEventsByPlayerName(destroyedPlayerName)
        moveTimerTask.addElement(DelayedExecution(
                {endGame(Pair(destroyedPlayerName,winnerName))},
                25,
        ))
    }

    private fun endGame( names: Pair<String, String>){
        timer.cancel()
        println("${names.first} was destroyed!!")
        println("${names.second} has won")
    }

}



