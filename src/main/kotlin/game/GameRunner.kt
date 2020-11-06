package game

import drawing.*
import helper.*
import java.awt.Color
import java.awt.Point
import java.util.*
import javax.swing.JFrame
import kotlin.random.Random

class GameConnector(
    private val frame: JFrame,
) :Movable {
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
        GameKeyEvent(P1_RIGHT, {playerOne.turningClock = true}, {playerOne.turningClock = false}),
        GameKeyEvent(P1_LEFT, {playerOne.turningCounterClock = true}, {playerOne.turningCounterClock = false}),
        GameKeyEvent(P1_FIRE, {playerOne.firing = true}, {playerOne.firing = false}),
        GameKeyEvent(P2_RIGHT, {playerTwo.turningClock = true}, {playerTwo.turningClock = false}),
        GameKeyEvent(P2_LEFT, {playerTwo.turningCounterClock = true}, {playerTwo.turningCounterClock = false}),
        GameKeyEvent(P2_FIRE, {playerTwo.firing = true}, {playerTwo.firing = false})
    )

    private val canvas: GameDrawCanvas = GameDrawCanvas(WINDOW_WIDTH, WINDOW_HEIGHT )
    private val keyListener: GameKeyListener
    private val moveTimerTask = MoveTimerTask()
    private val paintTimerTask = PaintTimerTask(::collectAndRepaint)
    private val animationTimerTask = AnimationTimerTask()
    private val timer = Timer()
    private val backgroundPixels : List<Pixel>



    init {
        // add Canvas to Frame
        canvas.background = Color.BLACK
        frame.add(canvas)

        // Create KeyListener and finish configuring frame
        keyListener = GameKeyListener(playerEvents)
        frame.addKeyListener(keyListener)
        frame.pack()

        // Create Background field
        backgroundPixels = createBackgroundStars()

        // Configure Timers
        timer.scheduleAtFixedRate(paintTimerTask, 500, PAINT_TIMER_INTERVAL)
        timer.scheduleAtFixedRate(moveTimerTask, 1000, MOVE_TIMER_INTERVAL)
        moveTimerTask.addElement(playerOne)
        moveTimerTask.addElement(playerOne.cannon)
        moveTimerTask.addElement(playerTwo)
        moveTimerTask.addElement(playerTwo.cannon)
        moveTimerTask.addElement(this)
        timer.scheduleAtFixedRate(animationTimerTask,1000, ANIMATION_TIMER_INTERVAL)
        animationTimerTask.addFunction(playerOne.cannon::switchAnimation)
        animationTimerTask.addFunction(playerTwo.cannon::switchAnimation)
        animationTimerTask.addFunction(playerOne.shield::switchAnimation)
        animationTimerTask.addFunction(playerTwo.shield::switchAnimation)
    }

    private fun collectAndRepaint(){
        canvas.shapesToDraw = playerOne.collectShapes() + playerTwo.collectShapes() + backgroundPixels //+ playerOne.cannon.collectPixels()
        canvas.debugTest = collectDebugMessages()
        canvas.repaint()
    }

    private fun collectDebugMessages(): List<String>{
        return listOf(
            "P1 RC count: ${playerOne.cannon.rechargeCounter}",
            "P1 Shots: ${playerOne.cannon.shots.size}",
            "P1 Shield: ${playerOne.shield.active}",
            "P1 Position: ${playerOne.centerToString()}"
        )
    }

    private fun createBackgroundStars(): List<Pixel>{
        val list = mutableListOf<Pixel>()
        for ( i in 0..AMOUNT_OF_STARS){
            val x = Random.nextInt(WINDOW_WIDTH - 20) + 10
            val y = Random.nextInt(WINDOW_HEIGHT - 20) + 10
            list.add(Pixel(STAR_COLOR, Point(x,y), STAR_SIZE))
        }
        return list
    }

    private fun end(playerName: String){
        println("$playerName was destroyed!!")
        timer.cancel()
    }

    // Connector added to Movables to perform collision checks
    override fun move() {
        fun checkCollision(p1: Point, p2: Point, d: Int): Boolean{
            return (calculateDistance(p1, p2) <= d)
        }
        fun hit(player: Ship){
            println("${player.name} was hit!!")
            player.hit(::end)
        }

        val playerOneShots = playerOne.cannon.collectPositions()
        playerOneShots.forEach {
            if (checkCollision(it, playerTwo.center, playerTwo.radius)) {
                hit(playerTwo)
            }
        }

        val playerTwoShots = playerTwo.cannon.collectPositions()
        playerTwoShots.forEach {
            if (checkCollision(it, playerOne.center, playerOne.radius)) {
                hit(playerOne)
            }
        }
    }
}



