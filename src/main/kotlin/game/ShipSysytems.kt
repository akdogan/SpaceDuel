package game

import drawing.Pixel
import helper.*
import java.awt.Color
import java.awt.Point

class Shield(private val shieldColor: List<Color>) {
    var active = false
    private var activeCounter = 0
    var powerLeft = SHIELD_MAX_CHARGE

    /**
     * Triggers the shields
     * @return Returns true, if the shield level dropped to 0 and thus the ship is destroyed
     */
    fun hit(): Boolean{
        active = true
        activeCounter = 0
        powerLeft--
        return powerLeft <= 0
    }

    fun getCurrentShieldColor(): Color{
        val i = activeCounter % shieldColor.size
        return shieldColor[i]
    }

    fun switchAnimation() {
        activeCounter++
        if (activeCounter >= SHIELD_VISIBLE_TIME){
            active = false
            activeCounter = 0
        }
    }
}



class LaserCannon : Movable {

    var shots: MutableList<LaserShot> = mutableListOf()
        private set
    private var animationState = AnimationState.ONE
    private var currentColor = LASER_COLOR_ONE
    private var charged = true
    var rechargeCounter = 0
        private set
    fun fire(pos: Point, vector: Vector){
        if (charged){
            shots.add(LaserShot(pos, vector))
            charged = false
        }
    }
    // TODO fragen: nachträglich Init ohne das die nullable sind?
    var getTarget: (() -> Pair<Point, Int>)? = null
    var hitTarget: ( () -> Unit )? = null

    fun switchAnimation(){
        animationState = animationState.next()
        currentColor = when (animationState){
            AnimationState.ONE -> LASER_COLOR_ONE
            AnimationState.TWO -> LASER_COLOR_TWO
            AnimationState.THREE -> LASER_COLOR_THREE
        }
    }

    fun collectPixels():List<Pixel>{
        return shots.map { Pixel(currentColor, it.pos, LASER_SHOT_SIZE) }
    }


    override fun move() {
        shots.forEach { it.move() }
        shots.removeIf { it.outOfBounds() }
        fun checkCollision(p1: Point, p2: Point, d: Int): Boolean{
            return (calculateDistance(p1, p2) <= d)
        }
        // Gets stats of the other ship
        val target = getTarget?.invoke()
        // target is nullable, therefore check required
        if (target != null){
            // checks collision, if true is returned, hit target function with the other ship is invoked
            if (shots.removeIf {
                    checkCollision(it.pos, target.first, target.second)}
                    ){
                hitTarget?.invoke()
                }
            }
        rechargeCounter++
        if (rechargeCounter >= LASER_RECHARGE_TIME){
            rechargeCounter = 0
            charged = true
        }
    }
}

class LaserShot(
    var pos: Point,
    private val vector: Vector,
    ) {

    fun move(){
        pos.location = calculatePoint(pos, vector)
    }

    fun outOfBounds(): Boolean{
        return (pos.x > WINDOW_WIDTH || pos.x < 0 || pos.y > WINDOW_HEIGHT || pos.y < 0)
    }
}