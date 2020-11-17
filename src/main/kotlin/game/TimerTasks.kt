package game

import java.util.*

class GameTimerTask() : TimerTask(){
    private val items : MutableList<() -> Unit> = mutableListOf()
    private var itemsToAdd : MutableList<() -> Unit> = mutableListOf()


    override fun run() {
        if (itemsToAdd.isNotEmpty()){
            items.addAll(itemsToAdd)
            itemsToAdd.clear()
        }
        items.forEach { it.invoke() }
    }

    operator fun plusAssign(item: () -> Unit){
        itemsToAdd.add(item)
    }
}

class SingleFunctionTimerTask(
        private val function: () -> Unit,
) : TimerTask() {
    override fun run() {
        function.invoke()
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