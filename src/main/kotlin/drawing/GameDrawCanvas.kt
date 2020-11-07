package drawing

import helper.DEBUG
import java.awt.*
import javax.swing.JPanel

val HUD_FONT = Font(Font.SANS_SERIF, Font.BOLD, 15)


class GameDrawCanvas(
    private val myHeight: Int,
    private val myWidth: Int,
    var shapesToDraw: List<GameShapes> = emptyList(),
    var debugTest : List<String> = emptyList()
) : JPanel() {


    override fun paintComponent(g: Graphics) {
        val ships : List<ShipOutLine> = shapesToDraw.filterIsInstance<ShipOutLine>()

        fun drawShipTriangle(element: ShipTriangle){
            g.fillPolygon(element.polygon)
        }
        fun drawShipCircle(element: ShipCircle){
            val g2 = g as Graphics2D
            if (element.outline){
                g2.stroke  = BasicStroke(5F)
                g2.drawOval(element.pos.x, element.pos.y, element.size, element.size)
            }
            else {
                g.fillOval(element.pos.x, element.pos.y, element.size, element.size)
            }

        }
        fun drawShipOutLine(element: ShipOutLine){
            val g2 = g as Graphics2D
            g2.stroke  = BasicStroke(5F)
            g2.drawLine(element.line)
            g2.drawPolygon(element.polygon)
        }
        fun drawPixel(element: Pixel){
            if (ships.none { it.polygon.contains(element.p)}) {
                g.fillOval(element.p.x, element.p.y, element.size, element.size)
            }
        }
        super.paintComponent(g)

        for ( shape in shapesToDraw){
            g.color = shape.color
            when (shape) {
                is ShipTriangle -> drawShipTriangle(shape)
                is ShipCircle -> drawShipCircle(shape)
                is ShipOutLine -> drawShipOutLine(shape)
                is Pixel -> drawPixel(shape)
            }
        }

        if (DEBUG){
            var debugY = 20
            g.color = Color.RED
            g.font = (Font(Font.SANS_SERIF, Font.BOLD, 18))
            for ( str in debugTest) {
                g.drawString(str, 20, debugY)
                debugY += 22
            }
        }
    }

    override fun getPreferredSize(): Dimension {
        return Dimension(myHeight, myWidth)
    }

}

fun Graphics.drawLine(line: HelperLine) = this.drawLine(
    line.p1.x,
    line.p1.y,
    line.p2.x,
    line.p2.y
)

