package drawing

import helper.*
import java.awt.Point
import kotlin.random.Random

class BackgroundStars {
    private val stars: MutableList<Pixel>

    init {
        stars = createBackgroundStars().toMutableList()
    }


    fun move() {
        stars.forEach { it.p.x -= STAR_SPEED }
        if (stars.removeIf { it.p.x <= 0 }){
            createNewStars()
        }
    }

    private fun createBackgroundStars(): List<Pixel>{
        val list = mutableListOf<Pixel>()
        repeat(AMOUNT_OF_STARS){
            val x = Random.nextInt(WINDOW_WIDTH - 20) + 10
            val y = Random.nextInt(WINDOW_HEIGHT - 20) + 10
            list.add(Pixel(STAR_COLOR, Point(x,y), STAR_SIZE))
        }
        return list
    }

    private fun createNewStars(){
        val starsToCreate = AMOUNT_OF_STARS - stars.size
        repeat(starsToCreate) {
            stars.add(Pixel(
                STAR_COLOR,
                Point(WINDOW_WIDTH, (Random.nextInt(WINDOW_HEIGHT -10 ) + 5)),
                STAR_SIZE
            ))
        }
    }

    fun collectShapes(): List<Pixel>{
        return stars.toList()
    }


}
