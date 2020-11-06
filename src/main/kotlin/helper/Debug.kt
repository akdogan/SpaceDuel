package helper

fun logger(str: String, localDebug: Boolean){
    if (DEBUG && localDebug){
        println(str)
    }
}