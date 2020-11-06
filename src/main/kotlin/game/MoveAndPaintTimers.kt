package game

import java.util.*

interface Movable {
    fun move()
}


class MoveTimerTask(
    private val movables : MutableList<Movable> = mutableListOf(),

    ) : TimerTask(){
    override fun run() {
        movables.forEach { it.move() }
    }

    fun addElement(m: Movable){
        movables.add(m)
    }

}

class PaintTimerTask(
    private val repainter: () -> Unit
) : TimerTask(){
    override fun run() {
        repainter.invoke()
    }

}

class AnimationTimerTask(

) : TimerTask(){
    private val functionList: MutableList<() -> Unit> = mutableListOf()

    override fun run() {
        functionList.forEach { it.invoke() }
    }

    fun addFunction(function: () -> Unit ){
        functionList.add(function)
    }
}

// TODO could be removed and solved in a different way
enum class AnimationState{
    ONE, TWO, THREE;

    fun next() : AnimationState{
        return when (this){
            ONE -> TWO
            TWO -> THREE
            THREE -> ONE
        }
    }
}