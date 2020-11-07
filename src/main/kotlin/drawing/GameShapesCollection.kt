package drawing

import java.awt.Color
import java.awt.Point
import java.awt.Polygon

sealed class GameShapes {
abstract val color: Color
}

class ShipTriangle(
    override val color: Color,
    val polygon: Polygon,
) : GameShapes()

class ShipCircle(
    override val color: Color,
    val pos: Point,
    val size: Int,
    val outline: Boolean
) : GameShapes()

class ShipOutLine(
    override val color: Color,
    val polygon: Polygon,
    val line: HelperLine,
) : GameShapes()

class Pixel(
    override val color: Color,
    val p: Point,
    val size: Int = 1
) : GameShapes()

class HelperLine(var p1: Point, var p2: Point){

}