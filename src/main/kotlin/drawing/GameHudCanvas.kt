package drawing

import helper.SHIELD_MAX_CHARGE
import helper.SIDE_MARGIN
import helper.WINDOW_WIDTH
import java.awt.*
import java.awt.Color.WHITE
import javax.swing.JPanel

class GameHudCanvas(
    private val myWidth: Int,
    private val myHeight: Int,
) : JPanel() {
    var labels: Pair<HudLabel, HudLabel> = Pair(
        HudLabel("", WHITE, 0),
        HudLabel("", WHITE, 0)
    )

    override fun paintComponent(g: Graphics) {
        fun drawShield(start: Int, charge: Int){
            val availableWidth = WINDOW_WIDTH / 2 - SIDE_MARGIN * 2
            val itemWidth = availableWidth / (SHIELD_MAX_CHARGE - 1)
            val itemHeight = 30
            val itemY = 60
            for ( i in 0 until SHIELD_MAX_CHARGE-1){
                if ( i < charge-1){
                    g.fill3DRect(
                        i * itemWidth + start,
                        itemY,
                        itemWidth - 5,
                        itemHeight,
                        true
                    )
                }
                else {
                    g.draw3DRect(
                        i * itemWidth + start,
                        itemY,
                        itemWidth - 5,
                        itemHeight,
                        true
                    )
                }

            }
        }
        super.paintComponent(g)
        g.font = (Font(Font.SANS_SERIF, Font.BOLD, 30))
        // Draw Player 1 Components
        g.color = labels.first.color
        val xp1 = SIDE_MARGIN
        g.drawString(labels.first.label, xp1, 40)
        drawShield(SIDE_MARGIN, labels.first.currentShieldCharge)

        //Draw Player 2 Components
        g.color = labels.second.color
        val xp2 = WINDOW_WIDTH / 2 + SIDE_MARGIN
        g.drawString(labels.second.label, xp2, 40)
        drawShield(xp2, labels.second.currentShieldCharge)

    }

    override fun getPreferredSize(): Dimension {
        return Dimension(myWidth, myHeight)
    }

}

class HudLabel(
    var label : String,
    var color: Color,
    var currentShieldCharge: Int
)