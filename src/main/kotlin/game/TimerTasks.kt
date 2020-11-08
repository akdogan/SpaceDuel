package game

import java.util.*

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

class DelayedExecution(
        // TODO Fragen: kann man einen Funktionsparameter definieren der egal welche Funktion nimmt?
        private val returnFunction: () -> Unit,
        private val maxCycles: Int,
) : Movable{
    private var counter = 0

    override fun move() {
        counter++
        if (counter >= maxCycles){
            returnFunction.invoke()
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