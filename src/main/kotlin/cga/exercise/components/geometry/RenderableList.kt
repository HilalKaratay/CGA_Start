package cga.exercise.components.geometry

import org.joml.Vector3f
import shader.ShaderProgram

class RenderableList(var objects : ArrayList<Renderable> = arrayListOf()){


    fun renderListOfObjects(shaderProgram: ShaderProgram){
        objects.forEach{it.render(shaderProgram)}
    }

    fun speedUpFixObjectt(){
        objects.forEach{it.translateGlobal(Vector3f(0f,0f,0f))}
    }
    fun speedUpObstacless(){
        objects.forEach{it.translateGlobal(Vector3f(0f,0f,0f))}
    }

    /*
    fun isFixObjectOnCam(floorMovement: FloorMovement){
        objects.forEach { floorMovement.isFloorOnCam(it) }
    }*/


    fun speedUpFixObject(speedMultiplier: Double){
        objects.forEach { it.translateGlobal(Vector3f(0f,0f,(speedMultiplier * 0.01).toFloat())) }
    }

/*
    fun isObstacleOnCam(objectMovement: ObjectMovement){
        objects.forEach { objectMovement.respawnObject(it) }
    }*/


    fun speedUpObstacles(speedMultiplier: Double){
        objects.forEach { it.translateGlobal(Vector3f(0f,0f,(speedMultiplier * 0.01).toFloat())) }
    }


}