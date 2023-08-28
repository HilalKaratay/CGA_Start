package cga.exercise.components.tiere

import cga.exercise.components.geometry.Renderable

abstract class Tier (){
    abstract val model: Renderable?
    abstract val radius:Float
    abstract var speed:Float


    open fun spawn():Boolean{
            setSpeed()
            return true
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