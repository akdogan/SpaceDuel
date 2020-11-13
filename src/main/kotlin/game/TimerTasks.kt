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
// Evtl. Beide Timer zu einem generischen Timer zusammenführen <T> fun und dann in run ausführen
class AnimationTimerTask(

) : TimerTask(){
    private val functionList: MutableList<() -> Unit> = mutableListOf()

    override fun run() {
        functionList.forEach { it.invoke() }
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

// TODO Fragen: kann man einen Funktionsparameter definieren der egal welche Funktion nimmt?
// TODO Could be replaced with object expression

class DelayedFunctionRunTask(
        private val returnFunction: () -> Unit,
) : TimerTask() {

    override fun run() {
        returnFunction.invoke()
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