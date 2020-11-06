package game

import helper.logger
import java.awt.Point
import kotlin.math.*
import kotlin.math.round

class Vector(var v1: Int, var v2: Int){
    constructor(a: Point, b: Point): this(calculateVector(a, b).first, calculateVector(a, b).second)

    /**
     * Returns a new Vector which is the opposite of this vector
     */
    fun reverse(): Vector{
        val db = false
        val newVector = Vector(-v1, -v2)

        logger("ReverseVector", db)
        logger("Old Vector: ($v1|$v2)", db)
        logger("New Vector: (${newVector.v1}|${newVector.v2})", db)

        return newVector
    }

    /**
     * Returns a new Vector 90° counter clockwise turned of this vector
     */
    fun turnCounterClock(): Vector{
        val v1New = -v2
        return Vector(v1 = v1New, v2 = v1 )
    }

    /**
     * Returns a new Vector 90° clockwise turned of this vector
     */
    fun turnClock(): Vector{
        val v1New = v2
        return Vector(v1 = v1New, v2 = -v1)
    }

    /**
     * Return a new Vector multiplied by the param
     * @param d amount to multiply this vector with. Use params <1 to shorten the vector
     * @return returns a new vector with direction of original but length modified by d
     */
    fun multiply(d: Double): Vector{
        return Vector(round(v1.toDouble() * d).toInt(), round(v2.toDouble() * d).toInt() )
    }

    /**
     * Changes the length of the Vector to exactly the length of d (rounded to INT)
     * To modify relatively to current length, use multiply
     * @param d the length of the new desired Vector
     * @return returns a new vector with the direction of the original vector and length d
     */
    fun changeLength(d: Double): Vector{
        val db = false
        logger("ChangeLength",db)
        logger("v1 Old: $v1", db)
        logger("v2 Old: $v2", db)

        val length = this.getLength()
        val v1New = (round(v1.toDouble() / length.toDouble() * d)).toInt()
        val v2New = (round(v2.toDouble() / length.toDouble() * d)).toInt()
        val newVector = Vector(v1New, v2New)
        logger("current Length: $length", db)
        logger("v1 New: $v1New", db)
        logger("v2 New: $v2New", db)
        logger("new length: ${newVector.getLength()}", db)

        return newVector
    }

    fun getLength(): Int{
       return calculateDistance(Point(0,0),Point(v1,v2))
    }
}

/**
 * Calculates the distance between two Points p1 and p2
 * This method has been added to provide greater accuracy and comfort, as the Point.distance(Point)
 * method included in AWT does not provide double precision
 * @return the distance between two points, mathematically rounded
 */
fun calculateDistance(p1: Point, p2: Point): Int {
    val db = false

    val a: Double = (p1.x - p2.x).toDouble()
    val b: Double = (p1.y - p2.y).toDouble()
    val result = sqrt( a.pow(2.0) + b.pow(2.0))
    logger("calculateDistance: P1(${p1.x}|${p1.y}), P2(${p2.x}|${p2.y})", db)
    logger("a: $a", db)
    logger("b: $b", db)
    logger("result: $result", db)
    logger("rounded: ${round(result).toInt()}", db)
    return round(result).toInt()

}

/**
 * Returns the Vector between two points
 */
fun calculateVector(p1: Point, p2: Point): Pair<Int, Int>{
    val ab1 = p2.x - p1.x
    val ab2 = p2.y - p1.y
    return Pair(ab1, ab2)
}

/**
 * creates a rotated point
 * @param p center point of the rotation
 * @param vector vector from the center point going to the point that should
 * be rotated around the center
 * @param degree degree of rotation (in degree, will be converted to radians)
 * @return
 */
fun rotate(p: Point, vector: Vector, degree: Double): Point{
    val alpha = Math.toRadians(degree)
    val pxTemp = round(((cos(alpha) * vector.v1) + (-sin(alpha) * vector.v2)) + p.x)
    val pyTemp = round(((sin(alpha) * vector.v1) + (cos(alpha)  * vector.v2)) + p.y)
    return Point(pxTemp.toInt(), pyTemp.toInt())
}

/**
 * Calculates and returns a new Point from a point and a vector
 * @param p A point from which the new point is calculated
 * @param v A vector which is added to point p to calculate the new Point. Exact length
 * of this vector is used to calculate the new point
 * @return New Point based on the calculation
 */
fun calculatePoint(p: Point, v: Vector): Point{
    val zeroB1 = p.x + v.v1
    val zeroB2 = p.y + v.v2

    return Point(zeroB1,zeroB2)
}
