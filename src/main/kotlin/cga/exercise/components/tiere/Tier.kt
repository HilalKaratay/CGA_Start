package cga.exercise.components.tiere

import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.Transformable
import org.joml.Vector3f
import kotlin.math.pow
import kotlin.random.Random

abstract class Tier (){
    abstract var isIdle:Boolean
    abstract var nextSpawn:Float
    abstract val model: Renderable?
    abstract val radius:Float
    abstract var speed:Float
    abstract val speedA:Int
    abstract val speedB:Int


    open fun spawn(t:Float):Boolean{
        if(nextSpawn<=t&&isIdle) {
            setSpeed()
            isIdle = false
            return true
        }else return false
    }


    open fun move(dt:Float){
      //  model?.rotate(20f,-1f,0f)
       // model?.translate(Vector3f(0f, 0f, -speed * dt))

    }
    fun getScaledRadius():Float{
        return radius*model!!.scaleFactor
    }


    open fun setSpeed() {
        speed= 2f
    }
}