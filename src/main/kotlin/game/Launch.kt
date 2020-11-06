package game

import javax.swing.JFrame



fun main() {
    // Create Frame
    val frame = JFrame("Test")
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.setSize(850, 850)
    frame.isVisible = true
    val gameConnector = GameConnector(frame)
}
