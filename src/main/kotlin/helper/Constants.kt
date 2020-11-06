package helper

import game.Vector
import java.awt.Color
import java.awt.Point

// GENERAL CONFIG
const val WINDOW_HEIGHT = 720
const val WINDOW_WIDTH = 1080
const val PAINT_TIMER_INTERVAL = 5L // Every 20ms -> 50 FPS
const val MOVE_TIMER_INTERVAL = 50L
const val ANIMATION_TIMER_INTERVAL = 200L
const val DEBUG = false


// SHIP CONFIGURATION
// Determines the size of the ship. Value is the length from center to tip
const val SHIP_SIZE = 60
// amount in degree to turn on each turnTimerAction
const val SHIP_TURN_AMOUNT = 10.0
// relative to ship vector length
const val SHIP_INITIAL_SPEED = 0.3
// buffer for the ship dissappearing from one side and reappearing on the other
const val SHIP_SCREEN_BOUND = 25
const val RELATIVE_START_X = 100
const val RELATIVE_START_Y = 200

// LASER AND SHIELDS
val LASER_COLOR_ONE: Color = Color.YELLOW
val LASER_COLOR_TWO: Color = Color.ORANGE
val LASER_COLOR_THREE: Color = Color.RED
const val LASER_SHOT_SIZE = 6
// number of cycles of the move timer until another shot is fired
const val LASER_RECHARGE_TIME = 8
const val LASER_SPEED = 0.8
const val SHIELD_MAX_CHARGE = 6
// number of cycles of the move timer the shields are visible
const val SHIELD_VISIBLE_TIME = 5

// PLAYER ONE VALUES
const val P1_NAME = "Player One"
val P1_LINE_COLOR: Color = Color(41, 100, 184)
val P1_FILL_COLOR = Color(114, 201, 214)
val P1_START_POINT = Point(RELATIVE_START_X, RELATIVE_START_Y) // Initial center point of the ship
val P1_START_VECTOR = Vector(SHIP_SIZE, 0) // v2 determines the direction
val P1_SHIELD_COLORS = listOf<Color>(
    Color.BLUE,
    Color.CYAN,
    Color.WHITE
)

// PLAYER TWO VALUES
const val P2_NAME = "Player Two"
val P2_LINE_COLOR: Color = Color(28, 133, 75)
val P2_FILL_COLOR: Color = Color(143, 237, 140)
val P2_START_POINT = Point(
    WINDOW_WIDTH - RELATIVE_START_X,
    WINDOW_HEIGHT - RELATIVE_START_Y
)
val P2_START_VECTOR = Vector(-SHIP_SIZE, 0)
val P2_SHIELD_COLORS = listOf<Color>(
    Color.GREEN,
    Color(125, 255, 125),
    Color.WHITE
)

// PLAYER KEYCODES
const val P1_LEFT = 65
const val P1_RIGHT = 68
const val P1_FIRE = 87
const val P2_LEFT = 37
const val P2_RIGHT = 39
const val P2_FIRE = 38

// BACKGROUND
const val AMOUNT_OF_STARS = 50
val STAR_COLOR: Color = Color.WHITE
const val STAR_SIZE = 4

// HUD
const val HUD_HEIGHT = 100
const val SIDE_MARGIN = 30
