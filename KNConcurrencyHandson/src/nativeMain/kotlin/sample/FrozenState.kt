package sample

import kotlin.native.concurrent.freeze
import kotlin.native.concurrent.isFrozen

fun freezeSomeState(){
    val sd = SomeData("Hello 🐶", 22)
    println("Am I frozen(bef)? ${sd.isFrozen}")
    sd.freeze()

    println("Am I frozen(aft)? ${sd.isFrozen}")
}

data class SomeData(val s:String, val i:Int)

fun failChanges(){
//    val smd = SomeMutableData2(3, null)
//    smd.i++
//    smd.smd = smd
    val smd = SomeMutableData(3)
    smd.i++
    println("smd: $smd")

    smd.freeze()
    smd.i++
    println("smd: $smd") //We won't actually get here
}
// data class SomeMutableData2(var i:Int, var smd:SomeMutableData2?)
data class SomeMutableData(var i:Int)

fun freezeChildren(){
    val dataWithReference = DataWithReference(SomeData("Hello 🐶", 22))
    dataWithReference.freeze()

    println("Am I frozen? ${dataWithReference.child.isFrozen}")
}

data class DataWithReference(val child:SomeData)