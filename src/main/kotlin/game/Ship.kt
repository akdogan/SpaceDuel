package game

import drawing.*
import helper.*
import java.awt.Color
import java.awt.Point
import java.awt.Polygon
import kotlin.math.round
import kotlin.math.sqrt

class Ship(
    val name: String,
    center: Point, //TODO Fragen: val hier definieren und private set machen
    private var vector: Vector,
    private val lineColor: Color,
    private val fillColor: Color,
    shieldColors: List<Color>
) : Movable{
    //TODO Some Properties could maybe be lazyinit
    var center = center
        private set
    private lateinit var tip: Point
    private lateinit var rear: Point
    private lateinit var leftWingTip: Point
    private lateinit var rightWingTip: Point
    private var velocity = vector.multiply(SHIP_INITIAL_SPEED)
    private var centerLine: Double = vector.getLength().toDouble() * 3.0 / 2.0
    private var sideLength: Double = round(2.0 * centerLine / sqrt(3.0))
    var radius = 0
        private set
    var turningClock = false
    var turningCounterClock = false
    var firing = false
    val cannon = LaserCannon()
    val shield = Shield(shieldColors)
    var exploding = false
    val explosion = Explosion()
    var active = true


    init {
        calculateShip()
        radius = calculateDistance(center, tip)
    }

    fun reset(p: Point, v: Vector){
        center = p
        vector = v
        calculateShip()
        exploding = true
        active = true
        cannon.reset()
        shield.reset()
        explosion.reset()

    }


    private fun calculateShip(){
        //calculateTip()
        tip = center + vector
        //calculateRear()
        rear = tip + vector.reverse().changeLength(centerLine)
        //calculateLeftWingTip()
        leftWingTip = rear + vector.turnClock().changeLength(round(sideLength * 0.5))
        //calculateRightWingTip()
        rightWingTip = rear + vector.turnCounterClock().changeLength(round(sideLength * 0.5))
        // calculate velocity
        velocity = vector.multiply(SHIP_INITIAL_SPEED)
        // calculate centerLine
        centerLine = vector.getLength().toDouble() * 3.0 / 2.0
        sideLength = round(2.0 * centerLine / sqrt(3.0))
    }

    fun collectShapes(): List<GameShapes>{
        val shapeList = mutableListOf<GameShapes>()
        if (active){
            if (!explosion.running) {
                val poly = Polygon()
                poly.addPoint(leftWingTip.x, leftWingTip.y)
                poly.addPoint(rear.x, rear.y)
                poly.addPoint(rightWingTip.x, rightWingTip.y)
                poly.addPoint(tip.x, tip.y)
                shapeList.add(ShipTriangle(fillColor, poly))
                shapeList.add(ShipOutLine(lineColor, poly, HelperLine(rear, tip)))
                if (shield.active){
                    shapeList.add(ShipCircle(
                            shield.getCurrentShieldColor(),
                            Point(center.x - radius, center.y - radius),
                            2 * radius,
                            true
                    ))
                }
                shapeList.addAll(cannon.collectPixels())
            }
            else {
                shapeList.add(ShipCircle(
                        explosion.getCurrentColor(),
                        Point(center.x - radius, center.y - radius),
                        2 * radius,
                        false
                ))
            }
        }
        return shapeList
    }


    override fun move() {
        val db = false
        logger("Before Move\n ${positionToString()}", db)
        if (active){
            center += velocity
            outOfScreen()
            calculateShip()
            if (turningClock) turn(SHIP_TURN_AMOUNT)
            if (turningCounterClock) turn(-SHIP_TURN_AMOUNT)
            calculateShip()
            if (firing) fire()
        }
        logger("After Move\n ${positionToString()}", db)
    }

    private fun turn(degree: Double){
        tip.location = rotate(center,vector,degree)
        vector = Vector(center, tip)
        //calculateShip()
    }

    private fun outOfScreen(){
        // TODO fragen: wie kann man daraus when machen
        val boundA = SHIP_SCREEN_BOUND
        if (center.x >= WINDOW_WIDTH + radius - boundA){
            center.x = 0 - radius + boundA
        }
        else if (center.x <= 0 - radius + boundA) {
            center.x = WINDOW_WIDTH + radius - boundA
        }
        else if (center.y >= WINDOW_HEIGHT + radius - boundA){
            center.y = 0 - radius + boundA
        }
        else if (center.y <= 0 - radius + boundA) {
            center.y = WINDOW_HEIGHT + radius - boundA
        }

    }

    private fun fire(){
        cannon.fire(Point(tip), vector.multiply(LASER_SPEED))
    }

    fun hit(triggerEndAnimation: (playerName: String, enemyName: String, explosion: Explosion) -> Unit){
        if (shield.hit()){
            explosion.running = true
            triggerEndAnimation.invoke(name, cannon.getTarget?.invoke()?.third?: "", explosion)
            exploding = true
        }
    }

    fun centerToString(): String{
        return "Center (${center.x} | ${center.y})"
    }

    private fun positionToString(): String{
        return """
            Before Move:
            rear: $rear
            tip: $tip
            Left: $leftWingTip
            Right: $rightWingTip
        """.trimIndent()
    }

    fun shutDownMovement() {
        turningCounterClock = false
        turningClock = false
        firing = false
    }

}