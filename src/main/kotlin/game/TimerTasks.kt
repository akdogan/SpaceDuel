package game

import java.util.*
import kotlin.reflect.KFunction1

interface Movable {
    fun move()
}


class MoveTimerTask(
    private val movables : MutableList<Movable> = mutableListOf(),
    private var movablesToAdd : MutableList<Movable> = mutableListOf()

    ) : TimerTask(){
    override fun run() {
        if (movablesToAdd.isNotEmpty()){
            movables.addAll(movablesToAdd)
            movablesToAdd.clear()
        }
        movables.forEach { it.move() }
    }

    fun addElement(m: Movable){
        movablesToAdd.add(m)
    }

}
// TODO Fragen: Zwei Konzepte, Interface implementieren (movable) oder Funktion
//  direkt zum ausführen geben (Animation Timer), was ist besser?
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

class PaintTimerTask(
    private val repainter: () -> Unit
) : TimerTask(){
    override fun run() {
        repainter.invoke()
    }

}

class DelayedExecution<T>(
        // TODO Fragen: kann man statt dem generischen Parameter T eine Funktion nehmen mit Any Paramter?
        private val returnFunction: (T) -> Any,
        private val maxCycles: Int,
        private val arg: T
) : Movable{
    private var counter = 0

    override fun move() {
        counter++
        if (counter >= maxCycles){
            returnFunction.invoke(arg)
        }
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