package cga.exercise.components.tiere

import cga.exercise.components.geometry.Renderable

abstract class Tier (){
    abstract var isIdle:Boolean
    abstract var createSpawn:Float
    abstract val model: Renderable?
    abstract val radius:Float
    abstract var speed:Float
    abstract val speedA:Int
    abstract val speedB:Int


    open fun spawn(t:Float):Boolean{
        if(createSpawn<=t&&isIdle) {
            setSpeed()
            isIdle = false
            return true
        }else return false
    }

    open fun move(dt:Float){
    }
    fun getScaledRadius():Float{
        return radius*model!!.scaleFactor
    }


    open fun setSpeed() {
        speed= 0.5f
    }
}