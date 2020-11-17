package game

import drawing.BackgroundStars
import drawing.GameDrawCanvas
import drawing.GameHudCanvas
import drawing.HudLabel
import game.State.ACTIVE
import game.State.INACTIVE
import helper.*
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Point
import java.util.*
import javax.swing.JFrame

class GameConnector(
        val frame: JFrame,
        private val keyListener: GameKeyListener,
        private val backgroundStars: BackgroundStars
) {
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
    private val inGameEvents: List<GameKeyEvent> = listOf(
        GameKeyEvent(P1_NAME, P1_RIGHT, {playerOne.turningClock = true}, {playerOne.turningClock = false}),
        GameKeyEvent(P1_NAME, P1_LEFT, {playerOne.turningCounterClock = true}, {playerOne.turningCounterClock = false}),
        GameKeyEvent(P1_NAME, P1_FIRE, {playerOne.firing = true}, {playerOne.firing = false}),
        GameKeyEvent(P2_NAME, P2_RIGHT, {playerTwo.turningClock = true}, {playerTwo.turningClock = false}),
        GameKeyEvent(P2_NAME, P2_LEFT, {playerTwo.turningCounterClock = true}, {playerTwo.turningCounterClock = false}),
        GameKeyEvent(P2_NAME, P2_FIRE, {playerTwo.firing = true}, {playerTwo.firing = false}),
        GameKeyEvent(SYSTEM_KEY_EVENT_NAME, START_RESTART, ::restart, {})
    )

    private val canvas = GameDrawCanvas(WINDOW_WIDTH, WINDOW_HEIGHT )
    private val hud = GameHudCanvas(WINDOW_WIDTH, HUD_HEIGHT)

    private val moveTimerTask = GameTimerTask()
    private val paintTimerTask = SingleFunctionTimerTask(::collectAndRepaint)
    private val animationTimerTask = GameTimerTask()
    private val timer = Timer()
    private var playerList = setOf(playerOne, playerTwo)


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
        frame.add(hud, BorderLayout.SOUTH)
        frame.add(canvas, BorderLayout.NORTH)
        backgroundStars.height = WINDOW_HEIGHT

        // Create KeyListener and finish configuring frame
        keyListener.removeAllEvents()
        keyListener.addEvents(inGameEvents.toMutableList())
        frame.pack()

        // Configure Targeting
        playerOne.cannon.getTarget = acquireTarget(playerTwo)
        playerOne.cannon.hitTarget = hitTarget(playerTwo)
        playerTwo.cannon.getTarget = acquireTarget(playerOne)
        playerTwo.cannon.hitTarget = hitTarget(playerOne)

        // Configure Timers TODO Delays could be removed
        timer.scheduleAtFixedRate(paintTimerTask, 0, PAINT_TIMER_INTERVAL)
        timer.scheduleAtFixedRate(moveTimerTask, 1000, MOVE_TIMER_INTERVAL)
        moveTimerTask += playerOne::move
        moveTimerTask += playerOne.cannon::move
        moveTimerTask += playerTwo::move
        moveTimerTask += playerTwo.cannon::move
        moveTimerTask += backgroundStars::move

        timer.scheduleAtFixedRate(animationTimerTask,0, ANIMATION_TIMER_INTERVAL)
        animationTimerTask += playerOne.cannon::switchAnimation
        animationTimerTask += playerTwo.cannon::switchAnimation
        animationTimerTask += playerOne.shield::switchAnimation
        animationTimerTask += playerTwo.shield::switchAnimation
        // GameKeyEvents are added with delay
        schedule(1650) {keyListener.switchEventsByName(ACTIVE, playerOne.name, playerTwo.name)}
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

    private fun schedule(delay: Int, function: ()-> Unit){
        timer.schedule(SingleFunctionTimerTask(function), delay.toLong())
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
        animationTimerTask += explosion::switchAnimation
        keyListener.switchEventsByName(INACTIVE, destroyedPlayerName)
        schedule(1250) {endGame(Pair(destroyedPlayerName,winnerName))}
    }

    private fun endGame( names: Pair<String, String>){
        //timer.cancel()
        println("${names.first} was destroyed!!")
        println("${names.second} has won")
        println("Press Enter to restart")
        // deactivate loosing player
        playerList.find { it.name == names.first }?.active = false
        playerList.forEach { it.shutDownMovement() }
        // deactivate winning player controls
        keyListener.switchEventsByName(INACTIVE, playerOne.name, playerTwo.name)
        // display Press Enter to restart TODO
        // activate "Enter to restart" event
        keyListener.switchEventsByName(ACTIVE, SYSTEM_KEY_EVENT_NAME)
        // Restart on "Enter" press
    }

    private fun restart() {
        // deactivate system key events
        keyListener.switchEventsByName(INACTIVE, SYSTEM_KEY_EVENT_NAME)
        // Reset both player positions
        playerOne.reset(P1_START_POINT, P1_START_VECTOR)
        playerTwo.reset(P2_START_POINT, P2_START_VECTOR)
        // activate loosing player / deactivate loosing player explosion status
        // reactivate controls of winning player
        schedule(650) {keyListener.switchEventsByName(ACTIVE, playerOne.name, playerTwo.name)}

    }

}



