package sample

import kotlin.native.concurrent.ensureNeverFrozen
import kotlin.native.concurrent.isFrozen

fun basicBackground(){
    println("Is main thread $isMainThread")
    background {
        println("Is main thread $isMainThread")
    }
}

fun captureState(){
    val sd = SomeData("Hello 🥶", 67)
    println("Am I frozen? ${sd.isFrozen}")
    background {
        println("Am I frozen now? ${sd.isFrozen}")
    }
}

fun captureTooMuch(){
    val model = CountingModel()
    model.increment()
    println("I have ${model.count}")

    model.increment()
    println("I have ${model.count}") //We won't get here
}

class CountingModel{
    var count = 0

    fun increment(){
        count++
        background {
            saveToDb(count)
        }
    }

    private fun saveToDb(arg:Int){
        //Do some db stuff
        println("Saving $arg to db")
    }
}

fun captureTooMuchAgain(){
    val model = CountingModel()
    println("model is frozen ${model.isFrozen}")
    model.increment()
    println("model is frozen ${model.isFrozen}")
}

fun captureArgs(){
    val model = CountingModelSafer()
    model.increment()
    println("I have ${model.state}")
    model.increment()
    println("I have ${model.state}")
}

class CountingModelSafer {
    var state = InternalState(0, InternalState2(10))

    fun increment() {
        state.count++
        state.state2.count++
        // Passes a deep-copied value to saveToDb for avoiding to freeze the entity of the state field.
        saveToDb(state.deepCopy())
    }

    private fun saveToDb(arg: InternalState) = background {
        println("Doing db stuff with ${arg.count}, in main $isMainThread")
        println("Doing db stuff with ${arg.state2.count}, in main $isMainThread")
    }
}

interface DeepCopyable<T> {
    fun deepCopy(): T
}

data class InternalState(var count: Int, val state2: InternalState2) : DeepCopyable<InternalState> {
    override fun deepCopy(): InternalState {
        return copy(state2 = state2.deepCopy())
    }
}

data class InternalState2(var count: Int) : DeepCopyable<InternalState2> {
    override fun deepCopy(): InternalState2 {
        return copy()
    }
}
