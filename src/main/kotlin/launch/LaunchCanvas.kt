package launch

import drawing.BackgroundStars
import helper.WINDOW_HEIGHT
import java.awt.*
import javax.swing.JPanel


class LaunchCanvas(
        val myHeight: Int,
        val myWidth: Int,
        val backgroundStars: BackgroundStars
) : JPanel() {


    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        for ( element in backgroundStars.collectShapes()){
            g.color = element.color
            g.fillOval(element.p.x, element.p.y, element.size, element.size)
        }

        val g2 = g as (Graphics2D)

        val title = "SpaceDuel"
        val message = "Press 'Space' to Start"

        val lineDistance = 100

        g2.color = Color.RED
        g2.font = (Font(Font.SANS_SERIF, Font.BOLD, 70))
        var y = WINDOW_HEIGHT / 4
        var stringWidth = g2.fontMetrics.stringWidth(title)
        var x = (myWidth / 2 ) - (stringWidth / 2)
        g2.drawString(title, x, y)

        g2.font = (Font(Font.SANS_SERIF, Font.BOLD, 50))
        y += lineDistance
        stringWidth = g2.fontMetrics.stringWidth(message)
        x = (myWidth / 2 ) - (stringWidth / 2)
        g2.drawString(message, x, y)

    }

    override fun getPreferredSize(): Dimension {
        return Dimension(myHeight, myWidth)
    }


}