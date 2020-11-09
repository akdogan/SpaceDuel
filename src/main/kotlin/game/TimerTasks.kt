package game

import java.util.*

interface Movable {
    fun move()
}

class MoveTimerTask(
        // Not sure why there is no concurrency problem with this method
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

    operator fun plusAssign(m: Movable){
        movablesToAdd.add(m)
    }

}
// TODO Fragen: Zwei Konzepte, Interface implementieren (movable) oder Funktion
//  direkt zum ausführen geben (Animation Timer), was ist besser?
// Evtl. Beide Timer zu einem generischen Timer zusammenführen
class AnimationTimerTask(

) : TimerTask(){
    private val functionList: MutableList<() -> Unit> = mutableListOf()

    override fun run() {
        functionList.forEach { it.invoke() }
    }

    fun addFunction(function: () -> Unit ){
        functionList.add(function)
    }

    operator fun plusAssign(function: () -> Unit ){
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
        // TODO Could be replaced with object expression
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